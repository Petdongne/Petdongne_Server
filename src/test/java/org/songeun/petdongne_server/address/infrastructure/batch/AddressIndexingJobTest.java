package org.songeun.petdongne_server.address.infrastructure.batch;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.songeun.petdongne_server.address.infrastructure.batch.adminDong.AdminDongAddressItemProcessor;
import org.songeun.petdongne_server.compare.infrastructure.batch.file.AdminDongAddressRow;
import org.songeun.petdongne_server.address.infrastructure.batch.legalDong.LegalDongAddressItemProcessor;
import org.songeun.petdongne_server.compare.infrastructure.batch.file.LegalDongAddressRow;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.*;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.repository.ElasticsearchAddressRepository;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.index.AddressIndexNameFactory;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.index.AddressIndexOperations;
import org.songeun.petdongne_server.compare.domain.AddressParts;
import org.songeun.petdongne_server.compare.domain.entity.AddressType;
import org.songeun.petdongne_server.compare.domain.AdminDongAddressParts;
import org.songeun.petdongne_server.compare.domain.LegalDongAddressParts;
import org.songeun.petdongne_server.global.batch.BatchProcessingException;
import org.songeun.petdongne_server.global.batch.policy.DeletedDataPolicy;
import org.songeun.petdongne_server.global.util.HashGenerator;
import org.songeun.petdongne_server.testSupport.FileUtils;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.NoSuchIndexException;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.songeun.petdongne_server.address.infrastructure.elasticsearch.error.AddressErrorStatus.ADDRESS_INDEX_NAME_DUPLICATED;
import static org.songeun.petdongne_server.compare.fixture.AddressFileFixtureFactory.*;

@Slf4j
@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
class AddressIndexingJobTest{

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private Job addressIndexingJob;

    @Autowired
    private ElasticsearchAddressRepository elasticsearchAddressRepository;

    @Autowired
    private AddressIndexOperations addressIndexRepository;

    @Autowired
    private LegalDongAddressItemProcessor legalDongAddressItemProcessor;

    @Autowired
    private AdminDongAddressItemProcessor adminDongAddressItemProcessor;

    @Autowired
    private DeletedDataPolicy legacyDataNotUsedPolicy;

    @TempDir
    private Path tempDir;

    @PostConstruct
    public void configureJobLauncherTestUtils() throws Exception {
        jobLauncherTestUtils.setJob(addressIndexingJob);
    }

    @AfterEach
    void afterAll() {
        Set<String> indexNamesByAlias;
        try {
            indexNamesByAlias = getAliasTargetIndexNames();
        } catch (NoSuchIndexException e) {
            return;
        }

        indexNamesByAlias.stream()
                .map(IndexCoordinates::of)
                .forEach(idx -> addressIndexRepository.deleteIndex(idx));
    }

    // job test

    @Test
    @DisplayName("새로운 주소 인덱스에 주소 데이터를 인덱싱한다.")
    void shouldIndexedWhenSuccessfullyJobCompleted() throws Exception {
        //given
        AddressFixtures testData = unique();
        AdminDongAddressFixture adminDongAddressFixture = testData.getAdminAddresses();
        LegalDongAddressFixture legalDongAddressFixture = testData.getLegalAddresses();

        Path legalDongFilePath = makeAddressFile("legalDong", legalDongAddressFixture.getRows());
        Path adminDongFilePath = makeAddressFile("adminDong", adminDongAddressFixture.getRows());

        String newIndexName = AddressIndexNameFactory.createAddressIndexName();
        JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                .addString("legalDongAddressFilePath", legalDongFilePath.toString())
                .addString("adminDongAddressFilePath", adminDongFilePath.toString())
                .addString("newIndexName", newIndexName)
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        //then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        assertNewIndexCreated(newIndexName);
//        assertAllIndexed(testData.extractLegalAddressIds(), AddressType.LEGAL_DONG_ADDRESS);
//        assertAllIndexed(testData.extractAdminAddressIds(), AddressType.ADMIN_DONG_ADDRESS);
    }

    private void assertNewIndexCreated(String newIndexName) {
        assertThat(addressIndexRepository
                .existIndex(IndexCoordinates.of(newIndexName)))
                .isTrue();
    }

    private void assertAllIndexed(List<String> addressIds, AddressType addressType) {
        addressIds.stream()
                .map(id -> elasticsearchAddressRepository.findById(id))
                .forEach(optionalAddress ->
                        {
                            assertThat(optionalAddress.isPresent()).isTrue();
                            assertThat(optionalAddress.get().getType()).isEqualTo(addressType);
                        }
                );
    }

    @Test
    @DisplayName("행정동 주소와 법정동 주소가 동일하면 법정동 주소만 인덱싱된다.")
    void shouldUniqueIndexedWhenDuplicatedFullAddress() throws Exception {
        //given
        AddressFixtures testData = duplicated();
        AdminDongAddressFixture adminDongAddressFixture = testData.getAdminAddresses();
        LegalDongAddressFixture legalDongAddressFixture = testData.getLegalAddresses();

        Path legalDongFilePath = makeAddressFile("legalDong", legalDongAddressFixture.getRows());
        Path adminDongFilePath = makeAddressFile("adminDong", adminDongAddressFixture.getRows());

        String newIndexName = AddressIndexNameFactory.createAddressIndexName();
        JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                .addString("legalDongAddressFilePath", legalDongFilePath.toString())
                .addString("adminDongAddressFilePath", adminDongFilePath.toString())
                .addString("newIndexName", newIndexName)
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        //then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        assertNewIndexCreated(newIndexName);
//        assertAllIndexed(testData.extractLegalAddressIds(), AddressType.LEGAL_DONG_ADDRESS);
//        assertUpsertAdminIfDuplicated(testData.extractAdminAddressIds());
    }

    private void assertUpsertAdminIfDuplicated(List<String> addressIds) {
        addressIds.stream()
                .map(id -> elasticsearchAddressRepository.findById(id))
                .forEach(optionalAddress ->
                        {
                            assertThat(optionalAddress.isPresent()).isTrue();
                            AddressDocument document = optionalAddress.get();
                            assertThat(document.getType()).isEqualTo(AddressType.LEGAL_DONG_ADDRESS);
                        }
                );
    }

    @Test
    @DisplayName("주소 데이터 처리 중 오류가 발생하면 이를 위해 생성했던 새 인덱스는 삭제한다.")
    void shouldIndexRollbackWhenGetChunkError() throws Exception {
        //given
        AddressFixtures testData = corrupted();
        AdminDongAddressFixture adminDongAddressFixture = testData.getAdminAddresses();
        LegalDongAddressFixture legalDongAddressFixture = testData.getLegalAddresses();

        Path adminDongFilePath = makeAddressFile("adminDong", adminDongAddressFixture.getRows());
        Path legalDongFilePath = makeAddressFile("legalDong", legalDongAddressFixture.getRows());

        String newIndexName = AddressIndexNameFactory.createAddressIndexName();
        JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                .addString("legalDongAddressFilePath", legalDongFilePath.toString())
                .addString("adminDongAddressFilePath", adminDongFilePath.toString())
                .addString("newIndexName", newIndexName)
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        //then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.FAILED);
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo(ExitStatus.FAILED.getExitCode());

         assertThat(addressIndexRepository.existIndex(IndexCoordinates.of(newIndexName))).isFalse();
    }

    // step test
    @Test
    @DisplayName("잡파라미터로 전달받은 인덱스 이름을 기반으로 인덱스를 생성한다.")
    void shouldCreateNewIndexWhenExecuteCreateNewAddressIndexStep(){
        //given
        String newIndexName = AddressIndexNameFactory.createAddressIndexName();
        JobParameters jobParams = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                .addString("newIndexName", newIndexName)
                .toJobParameters();

        try {
            //when
            JobExecution jobExecution = jobLauncherTestUtils.launchStep("createNewAddressIndexStep", jobParams);

            //then
            StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();

            assertThat(stepExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
            assertThat(addressIndexRepository.existIndex(IndexCoordinates.of(newIndexName))).isTrue();

        } finally {
            addressIndexRepository.deleteIndex(IndexCoordinates.of(newIndexName));
        }
    }

    @Test
    @DisplayName("중복된 이름의 인덱스를 생성하려고 하면 예외를 던진다.")
    void shouldThrowExceptionWhenIndexAlreadyExistsInCreateNewAddressIndexStep(){
        //given
        String newIndexName = AddressIndexNameFactory.createAddressIndexName();
        IndexCoordinates indexCoordinates = IndexCoordinates.of(newIndexName);

        try {
            log.info("try문 진입");
            // 생성하려는 인덱스 이름으로 인덱스 생성 -> 중복 유발
            addressIndexRepository.createIndex(indexCoordinates);
            log.info("createIndex 완료");

            JobParameters jobParams = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                    .addString("newIndexName", newIndexName)
                    .toJobParameters();

            //when
            JobExecution jobExecution = jobLauncherTestUtils.launchStep("createNewAddressIndexStep", jobParams);
            log.info("launchStep 완료");

            //then
            StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();

            assertThat(stepExecution.getStatus()).isEqualTo(BatchStatus.FAILED);
            assertThat(stepExecution.getExitStatus().getExitCode()).isEqualTo(ExitStatus.FAILED.getExitCode());
            assertThrownException(stepExecution.getFailureExceptions());

        } finally {
            addressIndexRepository.deleteIndex(indexCoordinates);
            log.info("deleteIndex 완료");
        }
    }

    private void assertThrownException(List<Throwable> failureExceptions) {
        assertThat(failureExceptions).hasSize(1);
        assertThat(failureExceptions.get(0))
                .isInstanceOf(BatchProcessingException.class)
                .withFailMessage(ADDRESS_INDEX_NAME_DUPLICATED.getMessage());
    }

    // 행정동 주소 데이터를 읽고 쓴 건수를 통해 인덱싱이 정상 수행됐음을 간접 확인
    @Test
    @DisplayName("행정동 주소 데이터가 유효하다면 정상적으로 처리한다.")
    void shouldAdminAddressProcessedWhenExecuteTwoStep() throws Exception {
        //given
        AdminDongAddressFixture adminDongData = uniqueAdminDong();
        Path adminDongFilePath = makeAddressFile("adminDong", adminDongData.getRows());

        String newIndexName = AddressIndexNameFactory.createAddressIndexName();
        JobParameters jobParams = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                .addString("adminDongAddressFilePath", adminDongFilePath.toString())
                .addString("newIndexName", newIndexName)
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("indexingAdminDongAddressStep", jobParams);

        //then
        StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();

        assertThat(stepExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(stepExecution.getReadCount()).isEqualTo(adminDongData.rowCount());
        assertThat(stepExecution.getWriteCount()).isEqualTo(adminDongData.rowCount());
    }

    @Test
    @DisplayName("말소된 행정동 주소는 인덱싱하지 않는다.")
    void shouldDeletedDataNotProcessedWhenExecuteIndexingAdminDongAddressStep() throws Exception {
        //given
        AdminDongAddressFixture adminDongData = deletedAdminDong(LocalDate.now());
        Path adminDongFilePath = makeAddressFile("adminDong", adminDongData.getRows());

        String newIndexName = AddressIndexNameFactory.createAddressIndexName();
        JobParameters jobParams = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                .addString("adminDongAddressFilePath", adminDongFilePath.toString())
                .addString("newIndexName", newIndexName)
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("indexingAdminDongAddressStep", jobParams);

        //then
        StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();

        assertThat(stepExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(stepExecution.getReadCount()).isEqualTo(adminDongData.rowCount());
        assertThat(stepExecution.getWriteCount()).isEqualTo(0);
    }

    // 법정동 주소 데이터를 읽고 쓴 건수를 통해 인덱싱이 정상 수행됐음을 간접 확인
    @Test
    @DisplayName("법정동 주소 데이터가 유효하다면 정상적으로 처리한다.")
    void shouldLegalAddressProcessWhenExecuteStep() throws Exception {
        //given
        LegalDongAddressFixture legalDongData = uniqueLegalDong();
        Path legalDongFilePath = makeAddressFile("legalDong", legalDongData.getRows());

        String newIndexName = AddressIndexNameFactory.createAddressIndexName();
        JobParameters jobParams = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                .addString("legalDongAddressFilePath", legalDongFilePath.toString())
                .addString("newIndexName", newIndexName)
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("indexingLegalDongAddressStep", jobParams);

        //then
        StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();

        assertThat(stepExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(stepExecution.getReadCount()).isEqualTo(legalDongData.rowCount());
        assertThat(stepExecution.getWriteCount()).isEqualTo(legalDongData.rowCount());
    }

    @Test
    @DisplayName("말소된 법정동 주소는 인덱싱하지 않는다.")
    void shouldDeletedDataNotProcessWhenExecuteIndexingLegalDongAddressStep() throws Exception {
        //given
        LegalDongAddressFixture legalDongData = deletedLegalDong(LocalDate.now());
        Path legalDongFilePath = makeAddressFile("legalDong", legalDongData.getRows());

        String newIndexName = AddressIndexNameFactory.createAddressIndexName();
        JobParameters jobParams = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                .addString("legalDongAddressFilePath", legalDongFilePath.toString())
                .addString("newIndexName", newIndexName)
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("indexingLegalDongAddressStep", jobParams);

        //then
        StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();

        assertThat(stepExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(stepExecution.getReadCount()).isEqualTo(legalDongData.rowCount());
        assertThat(stepExecution.getWriteCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("새로운 인덱스로 Alias 대상 인덱스를 변경한다.")
    void shouldSwitchIndexWhenExecuteSwitchAddressIndexAliasStep() {
        // given
        IndexCoordinates oldIndex = IndexCoordinates.of("old-address-index");
        addressIndexRepository.createIndex(oldIndex);
        addressIndexRepository.setAlias(oldIndex);

        IndexCoordinates newIndex = IndexCoordinates.of("new-address-index");
        try {
            addressIndexRepository.createIndex(newIndex);

            JobParameters jobParams = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                    .addString("newIndexName", newIndex.getIndexName())
                    .toJobParameters();

            Set<String> beforeTargetNames = getAliasTargetIndexNames();

            // when
            JobExecution jobExecution = jobLauncherTestUtils.launchStep("switchAliasTargetStep", jobParams);

            // then
            StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();
            assertThat(stepExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

            Set<String> afterTargetNames = getAliasTargetIndexNames();
            assertThat(afterTargetNames.size()).isOne();
            assertThat(afterTargetNames).doesNotContainAnyElementsOf(beforeTargetNames);
            assertThat(afterTargetNames).contains(newIndex.getIndexName());
        } finally {
            addressIndexRepository.deleteIndex(newIndex);
        }
    }


    @Test
    @DisplayName("행정동 데이터를 주소 인덱스 데이터로 변환한다.")
    void shouldProcessAdminAddressCorrectly() throws Exception {
        //given
        LocalDate creationDate = LocalDate.now();

        AdminDongAddressParts addressParts = AdminDongAddressParts.create("인천광역시", "남동구", "논현동");
        String id = createIdBy(addressParts, AddressType.ADMIN_DONG_ADDRESS);

        AdminDongAddressRow adminDongRow = AdminDongAddressRow.create("1111111111", addressParts.getSido(), addressParts.getSigungu(),
                addressParts.getEupmyeondong(), creationDate, null, legacyDataNotUsedPolicy);

        //when
        AddressDocument addressDocument = adminDongAddressItemProcessor.process(adminDongRow);

        //then
        assertThat(addressDocument)
                .extracting("id", "code", "sido", "sigungu", "eupmyeondong", "re", "fullAddress")
                .containsExactlyInAnyOrder(id, "1111111111", "인천광역시", "남동구", "논현동", null, "인천광역시 남동구 논현동");
    }

    private String createIdBy(AddressParts addressParts, AddressType addressType) {
        String fullAddress = addressParts.concatenateParts(" ");
        return HashGenerator.generate(fullAddress);
    }

    @Test
    @DisplayName("법정동 데이터를 주소 인덱스 데이터로 변환한다.")
    void shouldProcessLegalAddressCorrectly() throws Exception {
        //given
        LocalDate creationDate = LocalDate.of(1999, 1, 1);

        LegalDongAddressParts addressParts = LegalDongAddressParts.create("서울특별시", "강남구", "논현동", null);
        String id = createIdBy(addressParts, AddressType.LEGAL_DONG_ADDRESS);

        LegalDongAddressRow legalDongRow = LegalDongAddressRow.create("1111111111", addressParts.getSido(), addressParts.getSigungu(),
                addressParts.getEupmyeondong(), addressParts.getRe(), creationDate, null, legacyDataNotUsedPolicy);

        //when
        AddressDocument addressDocument = legalDongAddressItemProcessor.process(legalDongRow);

        //then
        assertThat(addressDocument)
                .extracting("id", "code", "sido", "sigungu", "eupmyeondong", "re", "fullAddress")
                .containsExactlyInAnyOrder(id, "1111111111", "서울특별시", "강남구", "논현동", null, "서울특별시 강남구 논현동");
    }


    @Test
    @DisplayName("말소된 행정동 데이터는 주소 인덱스 데이터로 변환하지 않는다.")
    void shouldNotProcessAdminAddress() throws Exception {
        //given
        LocalDate creationDate = LocalDate.of(1999, 1, 1);
        LocalDate deletedDate = LocalDate.now().minusDays(1);

        AdminDongAddressParts addressParts = AdminDongAddressParts.create("인천광역시", "남동구", "논현동");
        AdminDongAddressRow row = AdminDongAddressRow.create("1111111111", addressParts.getSido(), addressParts.getSigungu(),
                addressParts.getEupmyeondong(), creationDate, deletedDate, legacyDataNotUsedPolicy);

        //when
        AddressDocument addressDocument = adminDongAddressItemProcessor.process(row);

        //then
        assertThat(addressDocument).isNull();
    }

    @Test
    @DisplayName("말소된 법정동 데이터는 주소 인덱스 데이터로 변환하지 않는다.")
    void shouldNotProcessLegalAddress() throws Exception {
        //given
        LocalDate creationDate = LocalDate.of(1999, 1, 1);
        LocalDate deletedDate = LocalDate.now().minusDays(1);

        LegalDongAddressParts addressParts = LegalDongAddressParts.create("서울특별시", "강남구", "논현동", null);
        LegalDongAddressRow legalDongRow = LegalDongAddressRow.create("1111111111", addressParts.getSido(), addressParts.getSigungu(),
                addressParts.getEupmyeondong(), addressParts.getRe(), creationDate, deletedDate, legacyDataNotUsedPolicy);

        //when
        AddressDocument addressDocument = legalDongAddressItemProcessor.process(legalDongRow);

        //then
        assertThat(addressDocument).isNull();
    }

    private Set<String> getAliasTargetIndexNames() {
        return addressIndexRepository.findAliasTargetIndexNames();
    }

    private Path makeAddressFile(String fileName, List<List<String>> rows) throws Exception {
        Path legalDongFilePath = tempDir.resolve(fileName);
        createExcelFile(legalDongFilePath, rows);

        return legalDongFilePath;
    }

    private void createExcelFile(Path path, List<List<String>> rows) throws Exception {
        FileUtils.writeExcelFile(path, rows);
    }
    
}
package org.songeun.petdongne_server.building.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.building.fixture.BuildingFixtureFactory;

import static org.assertj.core.api.Assertions.assertThat;

class BuildingTest {

    @Test
    @DisplayName("유효한 승인일자에서 연도를 추출한다")
    void shouldExtractYearFromValidApprovalDate() {
        // Given
        Building building = createBuildingWithApprovalDate("20230115");

        // When
        String approvalYear = building.getApprovalYear();

        // Then
        assertThat(approvalYear).isEqualTo("2023");
    }

    @Test
    @DisplayName("승인일자가 null일 경우 null을 반환한다")
    void shouldReturnNullWhenApprovalDateIsNull() {
        // Given
        Building building = createBuildingWithApprovalDate(null);

        // When
        String approvalYear = building.getApprovalYear();

        // Then
        assertThat(approvalYear).isNull();
    }

    @Test
    @DisplayName("승인일자에서 연도를 추출할 수 없는 경우 null을 반환한다")
    void shouldReturnNullWhenCannotExtractYear() {
        // Given
        Building building = createBuildingWithApprovalDate("202");

        // When
        String approvalYear = building.getApprovalYear();

        // Then
        assertThat(approvalYear).isNull();
    }

    @Test
    @DisplayName("유효한 승인일자에서 월을 추출한다")
    void shouldExtractMonthFromValidApprovalDate() {
        // Given
        Building building = createBuildingWithApprovalDate("20231120");

        // When
        String approvalMonth = building.getApprovalMonth();

        // Then
        assertThat(approvalMonth).isEqualTo("11");
    }

    @Test
    @DisplayName("승인일자에서 월을 추출할 수 없는 경우 null을 반환한다")
    void shouldReturnNullWhenCannotExtractMonth() {
        // Given
        Building building = createBuildingWithApprovalDate("20230");

        // When
        String approvalMonth = building.getApprovalMonth();

        // Then
        assertThat(approvalMonth).isNull();
    }

    private Building createBuildingWithApprovalDate(String approvalDate) {
        try {
            return BuildingFixtureFactory.createBuilding(
                    "Test Building",
                    "12345678901234",
                    1,
                    100,
                    10,
                    1,
                    approvalDate,
                    "Test Jibun Address",
                    "testPnu",
                    "1234567",
                    "Test Road Name",
                    "123",
                    "45",
                    "11110",
                    BuildingUsage.APARTMENT,
                    127.0,
                    37.0
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to create building with approval date: " + approvalDate, e);
        }
    }

}

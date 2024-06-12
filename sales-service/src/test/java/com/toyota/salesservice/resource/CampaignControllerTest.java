package com.toyota.salesservice.resource;

import com.toyota.salesservice.dto.requests.CreateCampaignRequest;
import com.toyota.salesservice.dto.requests.UpdateCampaignRequest;
import com.toyota.salesservice.dto.responses.GetAllCampaignsResponse;
import com.toyota.salesservice.service.abstracts.CampaignService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CampaignControllerTest {
    @Mock
    private CampaignService campaignService;
    @InjectMocks
    private CampaignController campaignController;

    @Test
    void getAllCampaigns_shouldReturnListOfCampaigns() {
        // Given
        List<GetAllCampaignsResponse> mockResponses = Collections.singletonList(new GetAllCampaignsResponse());
        when(campaignService.getAllCampaigns()).thenReturn(mockResponses);

        // When
        ResponseEntity<List<GetAllCampaignsResponse>> responseEntity = campaignController.getAllCampaigns();

        // Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockResponses, responseEntity.getBody());
        verify(campaignService, times(1)).getAllCampaigns();
    }

    @Test
    void getCampaignByCampaignNumber_shouldReturnCampaign() {
        // Given
        String campaignNumber = "CAM123";
        GetAllCampaignsResponse mockResponse = new GetAllCampaignsResponse();
        when(campaignService.getCampaignByCampaignNumber(campaignNumber)).thenReturn(mockResponse);

        // When
        ResponseEntity<GetAllCampaignsResponse> responseEntity = campaignController.getCampaignByCampaignNumber(campaignNumber);

        // Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockResponse, responseEntity.getBody());
        verify(campaignService, times(1)).getCampaignByCampaignNumber(campaignNumber);
    }

    @Test
    void addCampaign_shouldReturnCreatedCampaign() {
        // Given
        CreateCampaignRequest request = new CreateCampaignRequest();
        GetAllCampaignsResponse mockResponse = new GetAllCampaignsResponse();
        when(campaignService.addCampaign(request)).thenReturn(mockResponse);

        // When
        ResponseEntity<GetAllCampaignsResponse> responseEntity = campaignController.addCampaign(request);

        // Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(mockResponse, responseEntity.getBody());
        verify(campaignService, times(1)).addCampaign(request);
    }

    @Test
    void updateCampaign_shouldReturnUpdatedCampaign() {
        // Given
        UpdateCampaignRequest request = new UpdateCampaignRequest();
        GetAllCampaignsResponse mockResponse = new GetAllCampaignsResponse();
        when(campaignService.updateCampaign(request)).thenReturn(mockResponse);

        // When
        ResponseEntity<GetAllCampaignsResponse> responseEntity = campaignController.updateCampaign(request);

        // Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockResponse, responseEntity.getBody());
        verify(campaignService, times(1)).updateCampaign(request);
    }

    @Test
    void deleteCampaign_shouldReturnDeletedCampaign() {
        // Given
        Long id = 1L;
        GetAllCampaignsResponse mockResponse = new GetAllCampaignsResponse();
        when(campaignService.deleteCampaign(id)).thenReturn(mockResponse);

        // When
        ResponseEntity<GetAllCampaignsResponse> responseEntity = campaignController.deleteCampaign(id);

        // Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockResponse, responseEntity.getBody());
        verify(campaignService, times(1)).deleteCampaign(id);
    }

    @Test
    void deleteAllCampaign_shouldReturnDeletionMessage() {
        // When
        String response = campaignController.deleteAllCampaign();

        // Then
        assertNotNull(response);
        assertEquals("Deletion successful!", response);
        verify(campaignService, times(1)).deleteAllCampaigns();
    }
}

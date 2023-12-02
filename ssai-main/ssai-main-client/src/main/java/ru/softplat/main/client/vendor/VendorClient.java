package ru.softplat.main.client.vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.softplat.main.client.BaseClient;
import ru.softplat.main.dto.vendor.VendorCreateUpdateDto;
import ru.softplat.main.dto.vendor.VendorSearchRequestDto;

import java.util.Map;

@Service
public class VendorClient extends BaseClient {
    private static final String API_PREFIX = "/vendor";

    @Autowired
    public VendorClient(@Value("${main-server.url:http://localhost:8080}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public ResponseEntity<Object> createVendor(VendorCreateUpdateDto vendorCreateUpdateDto) {
        return post("", vendorCreateUpdateDto);
    }

    public ResponseEntity<Object> changeVendorById(Long vendorId, VendorCreateUpdateDto vendorUpdateDto) {
        return patch("/" + vendorId, vendorUpdateDto);
    }

    public ResponseEntity<Object> findVendorWithFilers(VendorSearchRequestDto vendorSearchRequestDto, int minId, int pageSize) {
        Map<String, Object> parameters = Map.of(
                "minId", minId,
                "pageSize", pageSize
        );

        return get("/search?minId={minId}&pageSize={pageSize}", parameters);
    }

    public ResponseEntity<Object> findVendorById(Long vendorId) {
        return get("/" + vendorId);
    }

    public void deleteVendor(Long vendorId) {
        delete("/" + vendorId);
    }

    public ResponseEntity<Object> createVendorImage(Long vendorId, MultipartFile image) {
        return post("/" + vendorId + "/image", image);
    }

    public void deleteVendorImage(Long vendorId) {
        delete("/" + vendorId + "/image");
    }
}

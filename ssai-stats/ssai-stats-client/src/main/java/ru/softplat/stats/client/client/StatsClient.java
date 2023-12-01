package ru.softplat.stats.client.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.softplat.stats.dto.SortEnum;
import ru.softplat.stats.dto.StatsCreateDto;
import ru.softplat.stats.dto.StatsFilterAdmin;
import ru.softplat.stats.dto.StatsFilterSeller;

import java.util.Map;

@Service
public class StatsClient extends BaseClient {
    private static final String API_PREFIX = "/stats";

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public void addStats(StatsCreateDto statsCreateDto) {
        post("", statsCreateDto);
    }

    public ResponseEntity<Object> getSellerReportAdmin(StatsFilterAdmin statsFilterAdmin, SortEnum sort) {
    Map<String, Object> parameters = Map.of(
            "statsFilterAdmin", statsFilterAdmin,
            "sort", sort
        );

        return get("/admin/seller", parameters);
    }

    public ResponseEntity<Object> getProductReportAdmin(StatsFilterSeller statsFilterSeller, SortEnum sort) {
        Map<String, Object> parameters = Map.of(
                "statsFilterSeller", statsFilterSeller,
                "sort", sort
        );

        return get("/admin/product", parameters);
    }

    public ResponseEntity<Object> getProductsReportSeller(Long sellerId, StatsFilterSeller statsFilterSeller, SortEnum sort) {
        Map<String, Object> parameters = Map.of(
                "statsFilterSeller", statsFilterSeller,
                "sort", sort
        );

        return get("/seller", sellerId, parameters);
    }
}
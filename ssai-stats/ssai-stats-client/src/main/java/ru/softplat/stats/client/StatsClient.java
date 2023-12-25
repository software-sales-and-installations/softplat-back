package ru.softplat.stats.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.softplat.stats.dto.SortEnum;
import ru.softplat.stats.dto.StatsFilter;
import ru.softplat.stats.dto.StatsFilterSeller;
import ru.softplat.stats.dto.create.StatsCreateDemoDto;
import ru.softplat.stats.dto.create.StatsCreateDto;

import java.util.Map;

@Service
public class StatsClient extends BaseClient {
    private static final String API_PREFIX = "/stats";

    @Autowired
    public StatsClient(@Value("${stats-server.url:http://localhost:9090}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public void addStats(StatsCreateDto statsCreateDto) {
        post("", statsCreateDto);
    }

    public void addDemoStats(StatsCreateDemoDto statsCreateDemoDto) {
        post("/demo", statsCreateDemoDto);
    }

    public ResponseEntity<Object> getSellerReportAdmin(StatsFilter statsFilter, SortEnum sort) {
        Map<String, Object> parameters = Map.of(
                "sort", sort
        );

        return post("/admin?sort={sort}", null, parameters, statsFilter);
    }

    public ResponseEntity<Object> getProductReportSeller(Long sellerId, StatsFilterSeller statsFilterSeller, SortEnum sort) {
        Map<String, Object> parameters = Map.of(
                "sort", sort
        );

        return post("/seller?sort={sort}", sellerId, parameters, statsFilterSeller);
    }

    public ResponseEntity<Object> getFileReportAdmin(StatsFilter statsFilter, SortEnum sort) {
        Map<String, Object> parameters = Map.of(
                "sort", sort
        );

        return post("/admin/file?sort={sort}", null, parameters, statsFilter);
    }

    public ResponseEntity<Object> getFileReportSeller(Long sellerId, StatsFilterSeller statsFilterSeller, SortEnum sort) {
        Map<String, Object> parameters = Map.of(
                "sort", sort
        );

        return post("/seller/file?sort={sort}", sellerId, parameters, statsFilterSeller);
    }
}
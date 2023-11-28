package ru.softplat.model;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.softplat.StatsFilterAdmin;
import ru.softplat.StatsFilterSeller;
import ru.softplat.StatsResponseDto;
import ru.softplat.client.BaseClient;
import ru.softplat.message.LogMessage;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatsClient extends BaseClient {

    @Value("${stats-server.url}")
    private String serverUrl;

    @Value("${main-app.name}")
    private String appMain;

    @Autowired
    public StatsClient(RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory())
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getSellerReportAdmin(StatsFilterAdmin statsFilterAdmin,
                                                       @RequestParam(name = "sort", defaultValue = "POPULAR") SortEnum sort) {
        Map<String, Object> parameters = Map.of(
                "statsFilterAdmin", statsFilterAdmin,
                "sort", sort
        );

        return get(serverUrl + "/product/stats/admin/seller", parameters);
    }

    public ResponseEntity<Object> getProductReportAdmin(StatsFilterSeller statsFilterSeller,
                                                        @RequestParam(name = "sort", defaultValue = "POPULAR") SortEnum sort) {
        Map<String, Object> parameters = Map.of(
                "statsFilterSeller", statsFilterSeller,
                "sort", sort
        );

        return get(serverUrl + "/product/stats/admin/product", parameters);
    }

    public ResponseEntity<Object> getProductsReportSeller(String principalName,
                                                          StatsFilterSeller statsFilterSeller,
                                                          @RequestParam(name = "sort", defaultValue = "POPULAR") SortEnum sort) {
        Map<String, Object> parameters = Map.of(
                "email", principalName,
                "statsFilterSeller", statsFilterSeller,
                "sort", sort
        );

        return get(serverUrl + "/product/stats/seller", parameters);
    }


    public void addStats(HttpServletRequest request) {
        RequestDto requestDto = RequestDto.builder()
                .app(appMain)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .build();
        post(serverUrl + "/hit", requestDto);
    }

    public ResponseEntity<Object> stats(String start,
                                        String end,
                                        List<String> uris,
                                        boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );

        return get(serverUrl + "/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}
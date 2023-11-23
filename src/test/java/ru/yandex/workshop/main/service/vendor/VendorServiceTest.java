package ru.yandex.workshop.main.service.vendor;

import lombok.RequiredArgsConstructor;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.workshop.main.dto.vendor.VendorCreateUpdateDto;
import ru.yandex.workshop.main.dto.vendor.VendorSearchRequestDto;
import ru.yandex.workshop.main.model.vendor.Country;
import ru.yandex.workshop.main.model.vendor.Vendor;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class VendorServiceTest {
    private final EntityManager em;
    private final VendorService service;

    VendorCreateUpdateDto newVendorRequestOne;
    VendorCreateUpdateDto newVendorRequestTwo;
    Vendor newVendorResponseOne;
    Vendor newVendorResponseTwo;
    VendorCreateUpdateDto vendorUpdateDto;

    @BeforeEach
    void assistant() {
        newVendorRequestOne = VendorCreateUpdateDto.builder().name("testOne").description("testOne").country(Country.RUSSIA).build();
        newVendorRequestTwo = VendorCreateUpdateDto.builder().name("testTwo").description("testTwo").country(Country.INDIA).build();
        vendorUpdateDto = VendorCreateUpdateDto.builder().name("newTest").description("newTest").country(Country.USA).build();

        newVendorResponseOne = service.createVendor(newVendorRequestOne);
        newVendorResponseTwo = service.createVendor(newVendorRequestTwo);
    }

    @Test
    void createVendor_shouldReturnVendor_whenRequestDtoIsValid() {
        TypedQuery<Vendor> query = em.createQuery("Select v from Vendor AS v WHERE v.id = :vendorId", Vendor.class);
        Vendor vendor = query.setParameter("vendorId", newVendorResponseOne.getId()).getSingleResult();

        MatcherAssert.assertThat(vendor.getId(), notNullValue());
        MatcherAssert.assertThat(vendor.getDescription(), equalTo(newVendorResponseOne.getDescription()));
        MatcherAssert.assertThat(vendor.getCountry(), equalTo(newVendorResponseOne.getCountry()));
    }

    @Test
    void changeVendorById_shouldReturnUpdatedVendor_whenRequestDtoIsValid() {
        service.changeVendorById(newVendorResponseOne.getId(), vendorUpdateDto);

        TypedQuery<Vendor> query = em.createQuery("Select v from Vendor AS v WHERE v.id = :vendorId", Vendor.class);
        Vendor vendor = query.setParameter("vendorId", newVendorResponseOne.getId()).getSingleResult();

        MatcherAssert.assertThat(vendor.getId(), notNullValue());
        MatcherAssert.assertThat(vendor.getDescription(), equalTo(newVendorResponseOne.getDescription()));
        MatcherAssert.assertThat(vendor.getCountry(), equalTo(newVendorResponseOne.getCountry()));
    }

    @Test
    void findVendorAll_shouldReturnListOfVendors_whenSearchCriteria() {
        List<Vendor> response = service.findVendorsWithFilter(new VendorSearchRequestDto(null, List.of(Country.INDIA)), 0, 10);
        TypedQuery<Vendor> query = em.createQuery("Select v from Vendor AS v where v.country = :country", Vendor.class);
        List<Vendor> vendorList = query.setParameter("country", Country.INDIA).getResultList();

        MatcherAssert.assertThat(vendorList, notNullValue());
        MatcherAssert.assertThat(response, notNullValue());
        MatcherAssert.assertThat(vendorList.size(), equalTo(response.size()));
        MatcherAssert.assertThat(vendorList.get(0).getCountry(), equalTo(response.get(0).getCountry()));
        MatcherAssert.assertThat(vendorList.get(0).getName(), equalTo(response.get(0).getName()));
    }

    @Test
    void findVendorById_shouldReturnVendor_whenIdIsValid() {
        TypedQuery<Vendor> query = em.createQuery("Select v from Vendor AS v WHERE v.id = :vendorId", Vendor.class);
        Vendor vendor = query.setParameter("vendorId", newVendorResponseOne.getId()).getSingleResult();

        MatcherAssert.assertThat(vendor.getId(), notNullValue());
        MatcherAssert.assertThat(vendor.getDescription(), equalTo(newVendorResponseOne.getDescription()));
        MatcherAssert.assertThat(vendor.getCountry(), equalTo(newVendorResponseOne.getCountry()));
    }

    @Test
    void deleteVendor_shouldExecuteAndDeleteVendor() {
        service.deleteVendor(newVendorResponseOne.getId());

        TypedQuery<Vendor> query = em.createQuery("Select v from Vendor AS v", Vendor.class);
        List<Vendor> vendorList = query.getResultList();

        MatcherAssert.assertThat(vendorList, notNullValue());
        MatcherAssert.assertThat(vendorList.size(), equalTo(4));
    }
}

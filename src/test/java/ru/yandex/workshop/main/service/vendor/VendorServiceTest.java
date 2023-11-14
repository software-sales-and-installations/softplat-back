package ru.yandex.workshop.main.service.vendor;

import lombok.RequiredArgsConstructor;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.workshop.main.dto.vendor.VendorDto;
import ru.yandex.workshop.main.dto.vendor.VendorFilter;
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

    VendorDto newVendorRequestOne;
    VendorDto newVendorRequestTwo;
    Vendor newVendorResponseOne;
    Vendor newVendorResponseTwo;
    VendorDto vendorUpdateDto;

    @BeforeEach
    void assistant() {
        newVendorRequestOne = VendorDto.builder().name("testOne").description("testOne").country(Country.RUSSIA).build();
        newVendorRequestTwo = VendorDto.builder().name("testTwo").description("testTwo").country(Country.INDIA).build();
        vendorUpdateDto = VendorDto.builder().name("newTest").description("newTest").country(Country.USA).build();

        newVendorResponseOne = service.createVendor(newVendorRequestOne);
        newVendorResponseTwo = service.createVendor(newVendorRequestTwo);
    }

    @Test
    void createVendor() {
        TypedQuery<Vendor> query = em.createQuery("Select v from Vendor AS v WHERE v.id = :vendorId", Vendor.class);
        Vendor vendor = query.setParameter("vendorId", newVendorResponseOne.getId()).getSingleResult();

        MatcherAssert.assertThat(vendor.getId(), notNullValue());
        MatcherAssert.assertThat(vendor.getDescription(), equalTo(newVendorResponseOne.getDescription()));
        MatcherAssert.assertThat(vendor.getCountry(), equalTo(newVendorResponseOne.getCountry()));
    }

    @Test
    void changeVendorById() {
        service.changeVendorById(newVendorResponseOne.getId(), vendorUpdateDto);

        TypedQuery<Vendor> query = em.createQuery("Select v from Vendor AS v WHERE v.id = :vendorId", Vendor.class);
        Vendor vendor = query.setParameter("vendorId", newVendorResponseOne.getId()).getSingleResult();

        MatcherAssert.assertThat(vendor.getId(), notNullValue());
        MatcherAssert.assertThat(vendor.getDescription(), equalTo(newVendorResponseOne.getDescription()));
        MatcherAssert.assertThat(vendor.getCountry(), equalTo(newVendorResponseOne.getCountry()));
    }

    @Test
    void findVendorAll() {
        List<Vendor> response = service.findVendorsWithFilter(new VendorFilter(null, List.of(Country.INDIA)), 0, 10);
        TypedQuery<Vendor> query = em.createQuery("Select v from Vendor AS v where v.country = :country", Vendor.class);
        List<Vendor> vendorList = query.setParameter("country", Country.INDIA).getResultList();

        MatcherAssert.assertThat(vendorList, notNullValue());
        MatcherAssert.assertThat(response, notNullValue());
        MatcherAssert.assertThat(vendorList.size(), equalTo(response.size()));
        MatcherAssert.assertThat(vendorList.get(0).getCountry(), equalTo(response.get(0).getCountry()));
        MatcherAssert.assertThat(vendorList.get(0).getName(), equalTo(response.get(0).getName()));
    }

    @Test
    void findVendorById() {
        TypedQuery<Vendor> query = em.createQuery("Select v from Vendor AS v WHERE v.id = :vendorId", Vendor.class);
        Vendor vendor = query.setParameter("vendorId", newVendorResponseOne.getId()).getSingleResult();

        MatcherAssert.assertThat(vendor.getId(), notNullValue());
        MatcherAssert.assertThat(vendor.getDescription(), equalTo(newVendorResponseOne.getDescription()));
        MatcherAssert.assertThat(vendor.getCountry(), equalTo(newVendorResponseOne.getCountry()));
    }

    @Test
    void deleteVendor() {
        service.deleteVendor(newVendorResponseOne.getId());

        TypedQuery<Vendor> query = em.createQuery("Select v from Vendor AS v", Vendor.class);
        List<Vendor> vendorList = query.getResultList();

        MatcherAssert.assertThat(vendorList, notNullValue());
        MatcherAssert.assertThat(vendorList.size(), equalTo(4));
    }
}

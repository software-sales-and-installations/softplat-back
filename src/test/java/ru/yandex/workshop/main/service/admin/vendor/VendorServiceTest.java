package ru.yandex.workshop.main.service.admin.vendor;

import lombok.RequiredArgsConstructor;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.workshop.main.dto.vendor.VendorDto;
import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;
import ru.yandex.workshop.main.dto.vendor.VendorUpdateDto;
import ru.yandex.workshop.main.model.vendor.Country;
import ru.yandex.workshop.main.model.vendor.Vendor;
import ru.yandex.workshop.main.service.vendor.VendorService;

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

    VendorDto vendorDtoOne;
    VendorDto vendorDtoTwo;
    VendorResponseDto vendorResponseDto;
    VendorUpdateDto newVendorUpdateDto;
    VendorResponseDto newVendorResponseDto;

    @BeforeEach
    void assistant() {
        vendorDtoOne = VendorDto.builder().name("testOne").description("testOne").country(Country.RUSSIA).build();
        vendorDtoTwo = VendorDto.builder().name("testTwo").description("testTwo").country(Country.RUSSIA).build();
        newVendorUpdateDto = VendorUpdateDto.builder().name("newTest").description("newTest").imageId(1L).country(Country.USA).build();

        vendorResponseDto = service.createVendor(vendorDtoOne);
        newVendorResponseDto = service.createVendor(vendorDtoTwo);
    }

    @Test
    void createVendor() {
        TypedQuery<Vendor> query = em.createQuery("Select v from Vendor AS v WHERE v.id = :vendorId", Vendor.class);
        Vendor vendor = query.setParameter("vendorId", vendorResponseDto.getId()).getSingleResult();

        MatcherAssert.assertThat(vendor.getId(), notNullValue());
        MatcherAssert.assertThat(vendor.getDescription(), equalTo(vendorResponseDto.getDescription()));
        MatcherAssert.assertThat(vendor.getCountry(), equalTo(vendorResponseDto.getCountry()));
    }

    @Test
    void changeVendorById() {
        service.changeVendorById(vendorResponseDto.getId(), newVendorUpdateDto);

        TypedQuery<Vendor> query = em.createQuery("Select v from Vendor AS v WHERE v.id = :vendorId", Vendor.class);
        Vendor vendor = query.setParameter("vendorId", vendorResponseDto.getId()).getSingleResult();

        MatcherAssert.assertThat(vendor.getId(), notNullValue());
        MatcherAssert.assertThat(vendor.getDescription(), equalTo(newVendorUpdateDto.getDescription()));
        MatcherAssert.assertThat(vendor.getCountry(), equalTo(newVendorUpdateDto.getCountry()));
    }

    @Test
    void findVendorAll() {
        TypedQuery<Vendor> query = em.createQuery("Select v from Vendor AS v", Vendor.class);
        List<Vendor> vendorList = query.getResultList();

        MatcherAssert.assertThat(vendorList, notNullValue());
        MatcherAssert.assertThat(vendorList.size(), equalTo(10));
    }

    @Test
    void findVendorById() {
        TypedQuery<Vendor> query = em.createQuery("Select v from Vendor AS v WHERE v.id = :vendorId", Vendor.class);
        Vendor vendor = query.setParameter("vendorId", vendorResponseDto.getId()).getSingleResult();

        MatcherAssert.assertThat(vendor.getId(), notNullValue());
        MatcherAssert.assertThat(vendor.getDescription(), equalTo(vendorResponseDto.getDescription()));
        MatcherAssert.assertThat(vendor.getCountry(), equalTo(vendorResponseDto.getCountry()));
    }

    @Test
    void deleteVendor() {
        service.deleteVendor(vendorResponseDto.getId());

        TypedQuery<Vendor> query = em.createQuery("Select v from Vendor AS v", Vendor.class);
        List<Vendor> vendorList = query.getResultList();

        MatcherAssert.assertThat(vendorList, notNullValue());
        MatcherAssert.assertThat(vendorList.size(), equalTo(9));
    }
}
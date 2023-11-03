package ru.yandex.workshop.main.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(controllers = VendorController.class)
class VendorControllerTest {
//    @MockBean
//    VendorService service;
//    @Autowired
//    private MockMvc mvc;
//    @Autowired
//    ObjectMapper mapper;
//
//    static VendorDto vendorDto;
//    static VendorResponseDto vendorResponseDto;
//    static List<VendorResponseDto> vendorResponseDtoList;
//
//    @BeforeAll
//    static void assistant() {
//        vendorDto = VendorDto.builder().name("test").description("test").imageId(1L).country(Country.RUSSIA).build();
//        vendorResponseDto = VendorResponseDto.builder().id(1L).name("test").description("test").imageId(1L).country(Country.RUSSIA).build();
//        vendorResponseDtoList = List.of(vendorResponseDto, vendorResponseDto);
//    }
//
//    @Test
//    void createVendor() throws Exception {
//        when(service.createVendor(any()))
//                .thenReturn(vendorResponseDto);
//
//        mvc.perform(post("/admin/vendor")
//                        .content(mapper.writeValueAsString(vendorDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", is(vendorResponseDto.getId()), Long.class))
//                .andExpect(jsonPath("$.name", is(vendorResponseDto.getName().toString())))
//                .andExpect(jsonPath("$.description", is(vendorResponseDto.getDescription().toString())))
//                .andExpect(jsonPath("$.imageId", is(vendorResponseDto.getImageId()), Long.class))
//                .andExpect(jsonPath("$.country", is(vendorResponseDto.getCountry().toString())));
//    }
//
//    @Test
//    void changeVendorById() throws Exception {
//        when(service.changeVendorById(anyLong(), any()))
//                .thenReturn(vendorResponseDto);
//
//        mvc.perform(patch("/admin/vendor/1")
//                        .content(mapper.writeValueAsString(vendorDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(vendorResponseDto.getId()), Long.class))
//                .andExpect(jsonPath("$.name", is(vendorResponseDto.getName().toString())))
//                .andExpect(jsonPath("$.description", is(vendorResponseDto.getDescription().toString())))
//                .andExpect(jsonPath("$.imageId", is(vendorResponseDto.getImageId()), Long.class))
//                .andExpect(jsonPath("$.country", is(vendorResponseDto.getCountry().toString())));
//    }
//
//    @Test
//    void findVendor() throws Exception {
//        when(service.findVendorAll())
//                .thenReturn(vendorResponseDtoList);
//
//        mvc.perform(get("/vendor")
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(mapper.writeValueAsString(vendorResponseDtoList)));
//    }
//
//    @Test
//    void findVendorById() throws Exception {
//        when(service.findVendorById(anyLong()))
//                .thenReturn(vendorResponseDto);
//
//        mvc.perform(get("/vendor/1")
//                        .content(mapper.writeValueAsString(vendorDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(vendorResponseDto.getId()), Long.class))
//                .andExpect(jsonPath("$.name", is(vendorResponseDto.getName().toString())))
//                .andExpect(jsonPath("$.description", is(vendorResponseDto.getDescription().toString())))
//                .andExpect(jsonPath("$.imageId", is(vendorResponseDto.getImageId()), Long.class))
//                .andExpect(jsonPath("$.country", is(vendorResponseDto.getCountry().toString())));
//    }
//
////    @Test
////    void deleteVendor() throws Exception {
////        mvc.perform(delete("/admin/vendor/1")
////                        .characterEncoding(StandardCharsets.UTF_8)
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .accept(MediaType.APPLICATION_JSON))
////                .andExpect(status().isOk());
////    }
}
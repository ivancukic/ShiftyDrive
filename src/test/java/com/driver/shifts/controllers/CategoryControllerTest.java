package com.driver.shifts.controllers;

//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.driver.shifts.dto.CategoryDTO;
import com.driver.shifts.dto.UserDTO;
import com.driver.shifts.services.CategoryService;
import com.driver.shifts.services.JwtService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private CategoryService categoryService;
	
	@MockitoBean
	private JwtService jwtService;

	@MockitoBean
	private UserDetailsService userDetailsService;
	
	private CategoryDTO sampleCategory;
	
	@BeforeEach
	void setUp() {
		UserDTO userDTO = new UserDTO(1L, "Test User", "test@example.com", new Date(), new Date());
	    sampleCategory = new CategoryDTO(1L, "Test Cat", userDTO);
	}
	
	@Test
	@WithMockUser
    void testFindAllCategories_ShouldReturnList() throws Exception {
        List<CategoryDTO> categories = List.of(sampleCategory);

        when(categoryService.findAll()).thenReturn(categories);

        mockMvc.perform(get("/api/categories"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].id").value(1L))
               .andExpect(jsonPath("$[0].name").value("Test Cat"));
    }
	
	@Test
	@WithMockUser
    void testGetCategoryById_ValidId_ShouldReturnCategory() throws Exception {
        when(categoryService.findById(1L)).thenReturn(Optional.of(sampleCategory));

        mockMvc.perform(get("/api/categories/1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.name").value("Test Cat"));
    }
	
	@Test
	@WithMockUser
    void testGetCategoryById_NotFound_ShouldReturn404() throws Exception {
        when(categoryService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categories/1"))
               .andExpect(status().isNotFound());
    }
	
//	@Test
//	@WithMockUser(username = "testuser")
//    void testCreateCategory_ShouldReturnCreated() throws Exception {
//		UserDTO userDTO = new UserDTO(1L, "Test User", "test@example.com", new Date(), new Date());
//        CategoryDTO newCategory = new CategoryDTO(1L, "Test Cat 1", userDTO);
//        CategoryDTO savedCategory = new CategoryDTO(2L, "Test Cat 1", userDTO);
//
//        when(categoryService.save(any(CategoryDTO.class))).thenReturn(savedCategory);
//
//        mockMvc.perform(post("/api/categories")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("""
//                    {
//                      "name": "Test Cat 1"
//                    }
//                """))
//               .andExpect(status().isCreated())
//               .andExpect(jsonPath("$.id").value(2))
//               .andExpect(jsonPath("$.name").value("Test Cat 1"));
//    }
	
// @Test
// @WithMockUser
//    void testUpdateCategory_ShouldReturnUpdatedCategory() throws Exception {
//	 UserDTO userDTO = new UserDTO(1L, "Test User", "test@example.com", new Date(), new Date());
//        CategoryDTO updatedCategory = new CategoryDTO(1L, "Test Cat 1", userDTO);
//
//        when(categoryService.update(eq(1L), any(CategoryDTO.class))).thenReturn(updatedCategory);
//
//        mockMvc.perform(put("/api/categories/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("""
//                    {
//                      "name": "Updated"
//                    }
//                """))
//               .andExpect(status().isOk())
//               .andExpect(jsonPath("$.id").value(1))
//               .andExpect(jsonPath("$.name").value("Updated"));
//    }
	
//    @Test
//    @WithMockUser
//    void testDeleteCategory_ShouldReturnNoContent() throws Exception {
//        doNothing().when(categoryService).delete(1L);
//
//        mockMvc.perform(delete("/api/categories/1"))
//               .andExpect(status().isNoContent());
//    }


}

package bg.softuni.zooarchitect.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testViewIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(view().name("index"));
    }

    @Test
    public void testViewAbout() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(view().name("about"));
    }
}

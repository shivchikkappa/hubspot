package com.hubspot.resource;

import com.hubspot.config.TestResourceApplicationConfig;
import com.hubspot.resources.InvitationResource;
import com.hubspot.services.InvitationService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.easymock.EasyMock.reset;
import static org.junit.Assert.assertNotNull;

//@SpringBootTest(classes = {TestResourceApplicationConfig.class})
//@RunWith(SpringJUnit4ClassRunner.class)
public class InvitationResourceTest {

    private static InvitationService invitationService;

    private static MockMvc mockMvc;

    @BeforeClass
    public static void setup() {
        ApplicationContext appContext = new AnnotationConfigApplicationContext(TestResourceApplicationConfig.class);

        InvitationResource invitationResource = appContext.getBean("apiResource", InvitationResource.class);
        invitationService = appContext.getBean("apiService", InvitationService.class);

        mockMvc = MockMvcBuilders.standaloneSetup(invitationResource).build();
    }

    @Before
    public void before() {
        assertNotNull(invitationService);
        reset(invitationService);
    }
}

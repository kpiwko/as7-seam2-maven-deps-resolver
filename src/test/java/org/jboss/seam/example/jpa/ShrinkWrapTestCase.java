package org.jboss.seam.example.jpa;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.arquillian.ajocado.Ajocado;
import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.locator.IdLocator;
import org.jboss.arquillian.ajocado.utils.URLUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.jboss.shrinkwrap.resolver.api.maven.filter.ScopeFilter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@RunAsClient
public class ShrinkWrapTestCase {

    @Drone
    AjaxSelenium browser;

    // @ArquillianResource
    // URL contextPath;

    private static final IdLocator USER_FIELD = Ajocado.id("login:username");
    private static final IdLocator PASS_FIELD = Ajocado.id("login:password");
    private static final IdLocator LOGIN_BTN = Ajocado.id("login:login");
    private static final IdLocator STATUS_DIV = Ajocado.id("status");

    @Deployment
    public static WebArchive buildSeamJpaWar() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "jboss-seam-jpa.war");

        war.addPackage(Booking.class.getPackage());

        war.addAsResource(new File("src/main/resources/import.sql"), "import.sql");
        war.addAsResource(new File("src/main/resources/seam.properties"), "seam.properties");
        war.addAsResource(new File("src/main/resources/META-INF/persistence.xml"), "META-INF/persistence.xml");

        war.addAsWebInfResource(new File("src/main/webapp/WEB-INF/components.xml"));
        war.addAsWebInfResource(new File("src/main/webapp/WEB-INF/faces-config.xml"));
        war.addAsWebInfResource(new File("src/main/webapp/WEB-INF/jboss-deployment-structure.xml"));
        war.addAsWebInfResource(new File("src/main/webapp/WEB-INF/pages.xml"));

        war.setWebXML(new File("src/main/webapp/WEB-INF/web.xml"));

        war.addAsWebResource(new File("src/main/webapp/book.xhtml"));
        war.addAsWebResource(new File("src/main/webapp/confirm.xhtml"));
        war.addAsWebResource(new File("src/main/webapp/conversations.xhtml"));
        war.addAsWebResource(new File("src/main/webapp/home.xhtml"));
        war.addAsWebResource(new File("src/main/webapp/hotel.xhtml"));
        war.addAsWebResource(new File("src/main/webapp/index.html"));
        war.addAsWebResource(new File("src/main/webapp/main.xhtml"));
        war.addAsWebResource(new File("src/main/webapp/password.xhtml"));
        war.addAsWebResource(new File("src/main/webapp/register.xhtml"));
        war.addAsWebResource(new File("src/main/webapp/template.xhtml"));

        war.addAsDirectories("css", "img");
        war.addAsWebResource(new File("src/main/webapp/css/screen.css"), "css/screen.css");
        war.addAsWebResource(new File("src/main/webapp/css/trailblazer_main.css"), "trailblazer_main.css");

        war.addAsWebResource(new File("src/main/webapp/img/bg.gif"), "img/bg.gif");
        war.addAsWebResource(new File("src/main/webapp/img/btn.bg.gif"), "img/btn.bg.gif");
        war.addAsWebResource(new File("src/main/webapp/img/cal-next.png"), "img/cal-next.png");
        war.addAsWebResource(new File("src/main/webapp/img/cal-prev.png"), "img/cal-prev.png");
        war.addAsWebResource(new File("src/main/webapp/img/cnt.bg.gif"), "img/cnt.bg.gif");
        war.addAsWebResource(new File("src/main/webapp/img/dtpick.gif"), "img/dtpick.gif");
        war.addAsWebResource(new File("src/main/webapp/img/hdr.ad.jpg"), "img/hdr.ad.jpg");
        war.addAsWebResource(new File("src/main/webapp/img/hdr.bar.jpg"), "img/hdr.bar.jpg");
        war.addAsWebResource(new File("src/main/webapp/img/hdr.bg.gif"), "img/hdr.bg.gif");
        war.addAsWebResource(new File("src/main/webapp/img/hdr.title.gif"), "img/hdr.title.gif");
        war.addAsWebResource(new File("src/main/webapp/img/header_line.gif"), "img/header_line.gif");
        war.addAsWebResource(new File("src/main/webapp/img/input.bg.gif"), "img/input.bg.gif");
        war.addAsWebResource(new File("src/main/webapp/img/sdb.bg.gif"), "img/sdb.bg.gif");
        war.addAsWebResource(new File("src/main/webapp/img/spinner.gif"), "img/spinner.gif");
        war.addAsWebResource(new File("src/main/webapp/img/th.bg.gif"), "img/th.bg.gif");

        war.addAsLibraries(DependencyResolvers.use(MavenDependencyResolver.class).includeDependenciesFromPom("pom.xml")
                .resolveAs(GenericArchive.class, new ScopeFilter("compile", "runtime", "")));

        System.out.println("War content:\n" + war.toString(true));

        return war;

    }

    @Test
    public void login() throws Exception {
        Assert.assertTrue(true);
        browser.open(URLUtils.buildUrl(contextPath(), "home.seam"));

        browser.waitForPageToLoad();

        browser.type(USER_FIELD, "demo");
        browser.type(PASS_FIELD, "demo");

        Ajocado.waitForHttp(browser).click(LOGIN_BTN);

        Assert.assertTrue("Status element is present", browser.isElementPresent(STATUS_DIV));
        Assert.assertTrue("User demo was logged in", browser.getText(STATUS_DIV).contains("Welcome Demo User"));

    }

    // JMX protocol does not populate context path
    private URL contextPath() {
        try {
            return new URL("http://localhost:8080/jboss-seam-jpa/");
        } catch (MalformedURLException e) {
            throw new AssertionError("Unable to get context path");
        }
    }
}

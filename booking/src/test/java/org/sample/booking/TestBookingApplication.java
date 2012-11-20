package org.sample.booking;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByLinkText;
import org.openqa.selenium.By.ByTagName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.net.URL;

@RunWith(Arquillian.class)
public class TestBookingApplication extends Assert
{
   @Deployment(testable = false)
   public static WebArchive createDeployment()
   {
      WebArchive war = ShrinkWrap.create(WebArchive.class);
      war.setWebXML(TestBookingApplication.class.getResource("web.xml"));
      war.addAsWebInfResource(new File("src/main/webapp/WEB-INF/portlet.xml"));
      war.addAsWebResource(new File("src/main/webapp/public/javascripts/jquery-1.7.1.min.js"),
         "public/javascripts/jquery-1.7.1.min.js");
      war.addAsWebResource(new File("src/main/webapp/public/javascripts/jquery-ui-1.7.2.custom.min.js"),
         "public/javascripts/jquery-ui-1.7.2.custom.min.js");
      war.addAsWebResource(new File("src/main/webapp/public/javascripts/booking.js"), "public/javascripts/booking.js");
      war.addAsWebResource(new File("src/main/webapp/public/stylesheets/main.css"), "public/stylesheets/main.css");
      war.addAsWebResource(new File("src/main/webapp/public/ui-lightness/jquery-ui-1.7.2.custom.css"),
         "public/ui-lightness/jquery-ui-1.7.2.custom.css");
      return war;
   }

   @ArquillianResource
   URL deploymentURL;

   @Drone
   WebDriver driver;

   @Test
   @InSequence(value = 0)
   public void testHomePage() throws Exception
   {
      URL url = deploymentURL.toURI().resolve("embed/BookingPortlet").toURL();
      driver.get(url.toString());
      WebElement home = findElementBy(By.className("formLogin"));
      assertNotNull(home);
   }

   @Test
   @InSequence(value = 1)
   public void testRegister() throws Exception
   {
      URL url = deploymentURL.toURI().resolve("embed/BookingPortlet").toURL();
      driver.get(url.toString());
      WebElement registerForm = findElementBy(ByTagName.name("register"));
      assertNull(registerForm);
      WebElement album = findElementBy(ByLinkText.linkText("Register New User"));
      assertNotNull(album);
      album.click();
      registerForm = findElementBy(ByTagName.name("register"));
      assertNotNull(registerForm);
   }

   @Test
   @InSequence(value = 3)
   public void testLogin() throws Exception
   {
      URL url = deploymentURL.toURI().resolve("embed/BookingPortlet").toURL();
      driver.get(url.toString());
      assertNull(findElementBy(By.className("options")));
      WebElement form = findElementBy(By.className("formLogin"));
      assertNotNull(form);
      form.findElement(By.name("username")).sendKeys("demo");
      form.findElement(By.name("password")).sendKeys("demo");
      form.submit();
      assertNotNull(findElementBy(By.className("options")));
      driver.manage().deleteAllCookies();
   }

   @Test
   @InSequence(value = 4)
   public void testAfterLogin() throws Exception
   {
      URL url = deploymentURL.toURI().resolve("embed/BookingPortlet").toURL();
      Thread.sleep(3000);
      driver.get(url.toString());
      assertNull(findElementBy(By.className("options")));
   }

   private WebElement findElementBy(By by)
   {
      WebElement home;
      try
      {
         home = driver.findElement(by);
         return home;
      }
      catch (org.openqa.selenium.NoSuchElementException e)
      {
         return null;
      }
   }

}
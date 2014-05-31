package ac.il.technion.twc.api.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ac.il.technion.twc.api.ServiceSetup;
import ac.il.technion.twc.api.TwitterDataCenterBuilder.MissingPropertitesException;
import ac.il.technion.twc.api.TwitterDataCenterBuilder.NotAPropertyException;
import ac.il.technion.twc.api.TwitterDataCenterBuilder.NotAServiceException;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.Retweet;

/**
 * Test for {@link ServiceBuildingManagerTest}
 * 
 * @author Ziv Ronen
 * @date 30.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class ServiceBuildingManagerTest {

  private final List<BaseTweet> basesMock;
  private final List<Retweet> resMock;
  private static final int PREDEFINE_VAL = 5;
  private final ServiceBuildingManager $;

  /**
   * 
   */
  public final @Rule
  ExpectedException thrown = ExpectedException.none();

  /**
   * c'tor
   */
  @SuppressWarnings("unchecked")
  public ServiceBuildingManagerTest() {
    basesMock = mock(List.class);
    resMock = mock(List.class);
    $ = new ServiceBuildingManager();
    $.addProperty(SupportedProperty.class);
  }

  /**
   * @throws NotAServiceException
   * @throws MissingPropertitesException
   */
  @Test
  public final void checkOneConstructorsMockShouldNotThrowException()
      throws MissingPropertitesException, NotAServiceException {
    $.addQuery(OneConstructorsMock.class);
  }

  /**
   * @throws NotAServiceException
   * @throws MissingPropertitesException
   */
  @Test
  public final
      void
      checkClassWithOneAnnotatedConstructorAndOtherNotAnnotatedShouldNotThrowException()
          throws MissingPropertitesException, NotAServiceException {
    $.addQuery(OneConstructorsAnnotatedMock.class);
  }

  /**
   * @throws NotAServiceException
   * @throws MissingPropertitesException
   */
  @Test
  public final void checkClassWithSupportedClassShouldNotThrowException()
      throws MissingPropertitesException, NotAServiceException {
    $.addQuery(NeedSupportedProperty.class);
  }

  /**
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  @Test
  public final void
      getOneConstructorsAnnotatedMockShouldReturnObjectOfTheClass()
          throws InstantiationException, IllegalAccessException,
          IllegalArgumentException, InvocationTargetException {
    assertTrue($.getInstance(OneConstructorsAnnotatedMock.class) instanceof OneConstructorsAnnotatedMock);
  }

  /**
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  @Test
  public final void getOneConstructorsMockShouldReturnObjectOfTheClass()
      throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    assertTrue($.getInstance(OneConstructorsMock.class) instanceof OneConstructorsMock);
  }

  /**
   * @throws NotAServiceException
   * @throws MissingPropertitesException
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  @Test
  public final void getClassWithSupportedValueShouldUseSupportedValue()
      throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    final HashMap<Class<?>, Object> properties =
        new HashMap<Class<?>, Object>();
    properties.put(SupportedProperty.class, new SupportedProperty(
        new PredefineValue(PREDEFINE_VAL)));
    when(basesMock.size()).thenReturn(3);
    when(resMock.size()).thenReturn(2);
    $.setProperties(basesMock, resMock);

    assertEquals(
        5,
        ((NeedSupportedProperty) $.getInstance(NeedSupportedProperty.class)).value);
  }

  /**
   * @throws NotAServiceException
   * @throws MissingPropertitesException
   */
  @Test
  public final void checkTwoConstructorsMockShouldThrowNotAServiceException()
      throws MissingPropertitesException, NotAServiceException {
    thrown.expect(NotAServiceException.class);
    thrown.expectMessage(TwoConstructorsMock.class.getSimpleName()
        + " is not a service");
    $.addQuery(TwoConstructorsMock.class);
  }

  /**
   * @throws NotAServiceException
   * @throws MissingPropertitesException
   */
  @Test
  public final void checkAbstractMockShouldThrowNotAServiceException()
      throws MissingPropertitesException, NotAServiceException {
    thrown.expect(NotAServiceException.class);
    thrown.expectMessage(AbstractService.class.getSimpleName()
        + " is not a service");
    $.addQuery(AbstractService.class);
  }

  /**
   * @throws NotAServiceException
   * @throws MissingPropertitesException
   */
  @Test
  public final void
      checkClassWithZeroPublicConstructorsShouldThrowNotAServiceException()
          throws MissingPropertitesException, NotAServiceException {
    thrown.expect(NotAServiceException.class);
    thrown.expectMessage(ZeroPublicConstructorsMock.class.getSimpleName()
        + " is not a service");
    $.addQuery(ZeroPublicConstructorsMock.class);
  }

  /**
   * @throws NotAServiceException
   * @throws MissingPropertitesException
   */
  @Test
  public final void
      checkClassWithTwoAnnotatedConstructorsShouldThrowNotAServiceException()
          throws MissingPropertitesException, NotAServiceException {
    thrown.expect(NotAServiceException.class);
    thrown.expectMessage(TwoConstructorsAnnotatedMock.class.getSimpleName()
        + " is not a service");
    $.addQuery(TwoConstructorsAnnotatedMock.class);
  }

  /**
   * @throws MissingPropertitesException
   * @throws NotAServiceException
   */
  @Test
  public final
      void
      checkClassWithConstructorsThatNeedUnsupportedClassShouldThrowMissingPropertitesException()
          throws MissingPropertitesException, NotAServiceException {
    thrown.expect(MissingPropertitesException.class);
    thrown.expectMessage(servicePrefix(NotSupportedPropertyService.class)
        + missingMessage(ZeroPublicConstructorsMock.class,
            "path: NotSupportedPropertyService->ZeroPublicConstructorsMock"));
    $.addQuery(NotSupportedPropertyService.class);
  }

  /**
   * @throws MissingPropertitesException
   * @throws NotAServiceException
   */
  @Test
  public final
      void
      checkClassWithConstructorsThatNeedIntefaceShouldThrowMissingPropertitesException()
          throws MissingPropertitesException, NotAServiceException {
    thrown.expect(MissingPropertitesException.class);
    thrown.expectMessage(servicePrefix(NeedInterfacePropertyService.class)
        + notConcreteMessage(InterfaceProperty.class,
            "path: NeedInterfacePropertyService->InterfaceProperty"));
    $.addQuery(NeedInterfacePropertyService.class);
  }

  /**
   * @throws MissingPropertitesException
   * @throws NotAServiceException
   */
  @Test
  public final
      void
      checkClassWithConstructorsThatNeedAbstractClassShouldThrowMissingPropertitesException()
          throws MissingPropertitesException, NotAServiceException {
    thrown.expect(MissingPropertitesException.class);
    thrown.expectMessage(servicePrefix(NeedAbstractPropertyService.class)
        + notConcreteMessage(AbstractProperty.class,
            "path: NeedAbstractPropertyService->AbstractProperty"));
    $.addQuery(NeedAbstractPropertyService.class);
  }

  /**
   * @throws MissingPropertitesException
   * @throws NotAServiceException
   */
  @Test
  public final
      void
      checkClassWithConstructorsThatNeedPrimitveClassShouldThrowMissingPropertitesException()
          throws MissingPropertitesException, NotAServiceException {
    thrown.expect(MissingPropertitesException.class);
    thrown.expectMessage(servicePrefix(NeedPremitivePropertyService.class)
        + notConcreteMessage(byte.class,
            "path: NeedPremitivePropertyService->byte"));
    $.addQuery(NeedPremitivePropertyService.class);
  }

  /**
   * @throws MissingPropertitesException
   * @throws NotAServiceException
   */
  @Test
  public final
      void
      checkClassWithCircleTailThenCircleShouldThrowMissingPropertitesException()
          throws MissingPropertitesException, NotAServiceException {
    thrown.expect(MissingPropertitesException.class);
    thrown
        .expectMessage(servicePrefix(CircleHeadService.class)
            + circleMessage(
                CircleFirstProperty.class,
                "path: CircleHeadService->CircleFirstProperty->CircleSecondProperty->CircleThirdProperty->CircleFirstProperty"));
    $.addQuery(CircleHeadService.class);
  }

  /**
   * @throws MissingPropertitesException
   * @throws NotAServiceException
   */
  @Test
  public final void
      checkClassWithCircleShouldThrowMissingPropertitesException()
          throws MissingPropertitesException, NotAServiceException {
    thrown.expect(MissingPropertitesException.class);
    thrown
        .expectMessage(servicePrefix(CircleFirstProperty.class)
            + circleMessage(
                CircleSecondProperty.class,
                "path: CircleFirstProperty->CircleSecondProperty->CircleThirdProperty->CircleFirstProperty->CircleSecondProperty"));
    $.addQuery(CircleFirstProperty.class);
  }

  /**
   * @throws MissingPropertitesException
   * @throws NotAServiceException
   */
  @Test
  public final void
      checkClassThatNeedItselfShouldThrowMissingPropertitesException()
          throws MissingPropertitesException, NotAServiceException {
    thrown.expect(MissingPropertitesException.class);
    thrown.expectMessage(servicePrefix(SelfCircleService.class)
        + circleMessage(SelfCircleService.class,
            "path: SelfCircleService->SelfCircleService->SelfCircleService"));
    $.addQuery(SelfCircleService.class);
  }

  /**
   * @throws MissingPropertitesException
   * @throws NotAServiceException
   */
  @Test
  public final
      void
      checkClassThatHaveMultipleErrorShouldThrowMissingPropertitesExceptionWithMessageOnAll()
          throws MissingPropertitesException, NotAServiceException {
    thrown.expect(MissingPropertitesException.class);
    thrown
        .expectMessage(servicePrefix(MultipuleMissingCausesClass.class)
            + circleMessage(
                CircleFirstProperty.class,
                "path: MultipuleMissingCausesClass->CircleFirstProperty->CircleSecondProperty->CircleThirdProperty->CircleFirstProperty")
            + notConcreteMessage(char.class,
                "path: MultipuleMissingCausesClass->char")
            + notConcreteMessage(InterfaceProperty.class,
                "path: MultipuleMissingCausesClass->InterfaceProperty")
            + missingMessage(
                ZeroPublicConstructorsMock.class,
                "path: MultipuleMissingCausesClass->NotSupportedPropertyService->ZeroPublicConstructorsMock"));
    $.addQuery(MultipuleMissingCausesClass.class);
  }

  /**
   * 
   */
  @Test
  public final void checkPropertySucceed() {
    $.addProperty(SupportedProperty.class);
  }

  /**
   * 
   */
  @Test
  public final void checkPropertyFailed() {
    thrown.expect(NotAPropertyException.class);
    thrown.expectMessage(TwoConstructorsMock.class.getSimpleName()
        + " is not a property");
    $.addProperty(TwoConstructorsMock.class);
  }

  private String circleMessage(final Class<?> property, final String path) {
    return propertyPrefix(property) + "cause dependency circle. " + path + "\n";
  }

  private String notConcreteMessage(final Class<?> property, final String path) {
    return propertyPrefix(property)
        + "is not a concrete class (or it is a primitive). " + path + "\n";
  }

  private String missingMessage(final Class<?> property, final String path) {
    return propertyPrefix(property)
        + "doesn't possess identifible requested constructor. " + path
        + "\n\t(try to add a " + ServiceSetup.class.getSimpleName()
        + " Annotation to the requested constructor.)\n";
  }

  private String propertyPrefix(final Class<?> property) {
    return "- object of type " + property.getSimpleName()
        + " can't be created because it ";
  }

  private String servicePrefix(final Class<?> service) {
    return "The service " + service.getSimpleName()
        + " can't be register because:\n";
  }

  private static class ZeroPublicConstructorsMock {
    private ZeroPublicConstructorsMock() {

    }
  }

  private static class OneConstructorsMock {
    @ServiceSetup
    public OneConstructorsMock() {
    }
  }

  // read with reflection
  @SuppressWarnings("unused")
  private static class TwoConstructorsMock {
    public TwoConstructorsMock() {
    }

    public TwoConstructorsMock(final List<Retweet> res1,
        final List<Retweet> res2) {

    }
  }

  private static class OneConstructorsAnnotatedMock {
    // read with reflection
    @SuppressWarnings("unused")
    public OneConstructorsAnnotatedMock() {
    }

    @ServiceSetup
    public OneConstructorsAnnotatedMock(final OneConstructorsMock mock) {

    }
  }

  private static class TwoConstructorsAnnotatedMock {
    @ServiceSetup
    public TwoConstructorsAnnotatedMock() {
    }

    @ServiceSetup
    public TwoConstructorsAnnotatedMock(final OneConstructorsMock mock) {
    }
  }

  private static class NeedSupportedProperty {
    public final int value;

    @ServiceSetup
    public NeedSupportedProperty(final SupportedProperty a) {
      value = a.val.val;
    }
  }

  private static class SupportedProperty {
    public final PredefineValue val;

    // read with reflection
    @SuppressWarnings("unused")
    public SupportedProperty() {
      val = new PredefineValue(0);
    }

    // read with reflection
    @SuppressWarnings("unused")
    public SupportedProperty(final List<BaseTweet> bases,
        final List<Retweet> res) {
      val = new PredefineValue(bases.size() + res.size());
    }

    public SupportedProperty(final PredefineValue val) {
      this.val = val;
    }
  }

  private static class PredefineValue {
    public final int val;

    // read with reflection
    @SuppressWarnings("unused")
    public PredefineValue() {
      val = 0;
    }

    public PredefineValue(final int val) {
      this.val = val;
    }
  }

  private static class NotSupportedPropertyService {
    @ServiceSetup
    public NotSupportedPropertyService(final ZeroPublicConstructorsMock mock) {

    }
  }

  private static class NeedAbstractPropertyService {
    @ServiceSetup
    public NeedAbstractPropertyService(final AbstractProperty a) {
    }
  }

  private static class NeedInterfacePropertyService {
    @ServiceSetup
    public NeedInterfacePropertyService(final InterfaceProperty a) {
    }
  }

  private static class NeedPremitivePropertyService {
    @ServiceSetup
    public NeedPremitivePropertyService(final byte a) {
    }
  }

  private static abstract class AbstractProperty {
  }

  private static abstract class AbstractService {

    @ServiceSetup
    public AbstractService() {
    }
  }

  private static interface InterfaceProperty {
  }

  private static class SelfCircleService {
    @ServiceSetup
    public SelfCircleService(final SelfCircleService a) {
    }
  }

  private static class CircleHeadService {
    @ServiceSetup
    public CircleHeadService(final CircleFirstProperty a) {
    }
  }

  private static class CircleFirstProperty {
    @ServiceSetup
    public CircleFirstProperty(final CircleSecondProperty a) {
    }
  }

  private static class CircleSecondProperty {
    // read with reflection
    @SuppressWarnings("unused")
    public CircleSecondProperty(final CircleThirdProperty a) {
    }
  }

  private static class CircleThirdProperty {
    // read with reflection
    @SuppressWarnings("unused")
    public CircleThirdProperty(final CircleFirstProperty a) {
    }
  }

  private static class MultipuleMissingCausesClass {
    // read with reflection
    @ServiceSetup
    public MultipuleMissingCausesClass(final CircleFirstProperty a1,
        final char a2, final InterfaceProperty a3,
        final NotSupportedPropertyService a4) {
    }
  }

}

package ac.il.technion.twc.api.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ac.il.technion.twc.api.Property;
import ac.il.technion.twc.api.PropertyFactory;
import ac.il.technion.twc.api.QuerySetup;
import ac.il.technion.twc.api.TwitterDataCenterBuilder.MissingPropertitesException;
import ac.il.technion.twc.api.TwitterQuery;
import ac.il.technion.twc.api.TwitterQueryFactory;
import ac.il.technion.twc.api.TwitterQueryFactory.NotAQueryFactoryException;
import ac.il.technion.twc.api.tweet.BaseTweet;
import ac.il.technion.twc.api.tweet.ID;
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
   * @throws TwitterQuery.NotAQueryException
   * @throws MissingPropertitesException
   */
  @Test
  public final void checkOneConstructorsMockShouldNotThrowException()
      throws MissingPropertitesException, TwitterQuery.NotAQueryException {
    $.addQuery(OneConstructorsMock.class);
  }

  /**
   * @throws TwitterQuery.NotAQueryException
   * @throws MissingPropertitesException
   */
  @Test
  public final
      void
      checkClassWithOneAnnotatedConstructorAndOtherNotAnnotatedShouldNotThrowException()
          throws MissingPropertitesException, TwitterQuery.NotAQueryException {
    $.addQuery(OneConstructorsAnnotatedMock.class);
  }

  /**
   * @throws TwitterQuery.NotAQueryException
   * @throws MissingPropertitesException
   */
  @Test
  public final void checkClassWithSupportedClassShouldNotThrowException()
      throws MissingPropertitesException, TwitterQuery.NotAQueryException {
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
    $.addQuery(OneConstructorsAnnotatedMock.class);
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
    $.addQuery(OneConstructorsMock.class);
    assertTrue($.getInstance(OneConstructorsMock.class) instanceof OneConstructorsMock);
  }

  /**
   * @throws TwitterQuery.NotAQueryException
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
    $.addQuery(NeedSupportedProperty.class);
    assertEquals(
        5,
        ((NeedSupportedProperty) $.getInstance(NeedSupportedProperty.class)).value);
  }

  /**
   * @throws TwitterQuery.NotAQueryException
   * @throws MissingPropertitesException
   */
  @Test
  public final void checkTwoConstructorsMockShouldThrowNotAServiceException()
      throws MissingPropertitesException, TwitterQuery.NotAQueryException {
    thrown.expect(TwitterQuery.NotAQueryException.class);
    thrown.expectMessage(TwoConstructorsMock.class.getSimpleName()
        + " is not a service");
    $.addQuery(TwoConstructorsMock.class);
  }

  /**
   * @throws TwitterQuery.NotAQueryException
   * @throws MissingPropertitesException
   */
  @Test
  public final void checkAbstractMockShouldThrowNotAServiceException()
      throws MissingPropertitesException, TwitterQuery.NotAQueryException {
    thrown.expect(TwitterQuery.NotAQueryException.class);
    thrown.expectMessage(AbstractService.class.getSimpleName()
        + " is not a service");
    $.addQuery(AbstractService.class);
  }

  /**
   * @throws TwitterQuery.NotAQueryException
   * @throws MissingPropertitesException
   */
  @Test
  public final void
      checkClassWithZeroPublicConstructorsShouldThrowNotAServiceException()
          throws MissingPropertitesException, TwitterQuery.NotAQueryException {
    thrown.expect(TwitterQuery.NotAQueryException.class);
    thrown.expectMessage(ZeroPublicConstructorsMock.class.getSimpleName()
        + " is not a service");
    $.addQuery(ZeroPublicConstructorsMock.class);
  }

  /**
   * @throws TwitterQuery.NotAQueryException
   * @throws MissingPropertitesException
   */
  @Test
  public final void
      checkClassWithTwoAnnotatedConstructorsShouldThrowNotAServiceException()
          throws MissingPropertitesException, TwitterQuery.NotAQueryException {
    thrown.expect(TwitterQuery.NotAQueryException.class);
    thrown.expectMessage(TwoConstructorsAnnotatedMock.class.getSimpleName()
        + " is not a service");
    $.addQuery(TwoConstructorsAnnotatedMock.class);
  }

  /**
   * @throws MissingPropertitesException
   * @throws TwitterQuery.NotAQueryException
   */
  @Test
  public final
      void
      checkClassWithConstructorsThatNeedUnsupportedClassShouldThrowMissingPropertitesException()
          throws MissingPropertitesException, TwitterQuery.NotAQueryException {
    thrown.expect(MissingPropertitesException.class);
    thrown.expectMessage(servicePrefix(NotSupportedPropertyService.class)
        + missingMessage(ZeroPublicConstructorsMock.class,
            "path: NotSupportedPropertyService->ZeroPublicConstructorsMock"));
    $.addQuery(NotSupportedPropertyService.class);
  }

  /**
   * @throws MissingPropertitesException
   * @throws TwitterQuery.NotAQueryException
   */
  @Test
  public final
      void
      checkClassWithConstructorsThatNeedIntefaceShouldThrowMissingPropertitesException()
          throws MissingPropertitesException, TwitterQuery.NotAQueryException {
    thrown.expect(MissingPropertitesException.class);
    thrown.expectMessage(servicePrefix(NeedInterfacePropertyService.class)
        + notConcreteMessage(InterfaceProperty.class,
            "path: NeedInterfacePropertyService->InterfaceProperty"));
    $.addQuery(NeedInterfacePropertyService.class);
  }

  /**
   * @throws MissingPropertitesException
   * @throws TwitterQuery.NotAQueryException
   */
  @Test
  public final
      void
      checkClassWithConstructorsThatNeedAbstractClassShouldThrowMissingPropertitesException()
          throws MissingPropertitesException, TwitterQuery.NotAQueryException {
    thrown.expect(MissingPropertitesException.class);
    thrown.expectMessage(servicePrefix(NeedAbstractPropertyService.class)
        + notConcreteMessage(AbstractProperty.class,
            "path: NeedAbstractPropertyService->AbstractProperty"));
    $.addQuery(NeedAbstractPropertyService.class);
  }

  /**
   * @throws MissingPropertitesException
   * @throws TwitterQuery.NotAQueryException
   */
  @Test
  public final
      void
      checkClassWithConstructorsThatNeedPrimitveClassShouldThrowMissingPropertitesException()
          throws MissingPropertitesException, TwitterQuery.NotAQueryException {
    thrown.expect(MissingPropertitesException.class);
    thrown.expectMessage(servicePrefix(NeedPremitivePropertyService.class)
        + notConcreteMessage(byte.class,
            "path: NeedPremitivePropertyService->byte"));
    $.addQuery(NeedPremitivePropertyService.class);
  }

  /**
   * @throws MissingPropertitesException
   * @throws TwitterQuery.NotAQueryException
   */
  @Test
  public final
      void
      checkClassWithCircleTailThenCircleShouldThrowMissingPropertitesException()
          throws MissingPropertitesException, TwitterQuery.NotAQueryException {
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
   * @throws TwitterQuery.NotAQueryException
   */
  @Test
  public final void
      checkClassWithCircleShouldThrowMissingPropertitesException()
          throws MissingPropertitesException, TwitterQuery.NotAQueryException {
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
   * @throws TwitterQuery.NotAQueryException
   */
  @Test
  public final void
      checkClassThatNeedItselfShouldThrowMissingPropertitesException()
          throws MissingPropertitesException, TwitterQuery.NotAQueryException {
    thrown.expect(MissingPropertitesException.class);
    thrown.expectMessage(servicePrefix(SelfCircleService.class)
        + circleMessage(SelfCircleService.class,
            "path: SelfCircleService->SelfCircleService->SelfCircleService"));
    $.addQuery(SelfCircleService.class);
  }

  /**
   * @throws MissingPropertitesException
   * @throws TwitterQuery.NotAQueryException
   */
  @Test
  public final
      void
      checkClassThatHaveMultipleErrorShouldThrowMissingPropertitesExceptionWithMessageOnAll()
          throws MissingPropertitesException, TwitterQuery.NotAQueryException {
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
    thrown.expect(Property.NotAPropertyException.class);
    thrown.expectMessage(TwoConstructorsMock.class.getSimpleName()
        + " is not a property");
    $.addProperty(TwoConstructorsMock.class);
  }

  /**
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InstantiationException
   * 
   */
  @Test
  public final void propertyFactorySucceed() throws InstantiationException,
      IllegalAccessException, IllegalArgumentException,
      InvocationTargetException {
    $.addProperty(MyProperty.class, new MySuperPropertyFactory(5));
    final List<Retweet> rs = Collections.emptyList();
    final List<BaseTweet> asList =
        Arrays.asList(new BaseTweet(new Date(0L), new ID("1")));
    $.setProperties(asList, rs);
    $.addQuery(MyQuery.class);
    assertEquals(asList.toString() + rs.toString() + 5,
        ((MyQuery) $.getInstance(MyQuery.class)).pr.toString());
  }

  /**
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InstantiationException
   * 
   */
  @Test
  public final void queryFactory() throws InstantiationException,
      IllegalAccessException, IllegalArgumentException,
      InvocationTargetException {
    $.addProperty(MyProperty.class, new MySuperPropertyFactory(5));
    $.addProperty(MySuperProperty.class, new MySuperPropertyFactory(6));
    $.addQuery(MyQuery.class, new MySuperQueryFactory(4));
    final List<Retweet> rs = Collections.emptyList();
    final List<BaseTweet> bs = Collections.emptyList();
    $.setProperties(bs, rs);
    final MySuperQuery q = (MySuperQuery) $.getInstance(MyQuery.class);
    assertEquals(4, q.val);
    assertEquals(6, q.spr.val);
    assertEquals(5, ((MySuperProperty) q.pr).val);
  }

  /**
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InstantiationException
   * 
   */
  @Test
  public final void factoryWithNoGetShouldThrowNotAQueryFactoryException()
      throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    thrown.expect(NotAQueryFactoryException.class);
    thrown.expectMessage(factoryErrorMessage(
        NoGetFactory.class.getSimpleName(), "have no get method"));
    $.addQuery(MyQuery.class, new NoGetFactory());
  }

  /**
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   */
  @Test
  public final void factoryWithTwoGetShouldThrowNotAQueryFactoryException()
      throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    thrown.expect(NotAQueryFactoryException.class);
    thrown.expectMessage(factoryErrorMessage(
        TwoGetFactory.class.getSimpleName(), "have multiple get function."));
    $.addQuery(MyQuery.class, new TwoGetFactory());
  }

  /**
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   */
  @Test
  public final
      void
      factoryWithGetThatRequireNonPropertyTypesShouldThrowNotAQueryFactoryException()
          throws InstantiationException, IllegalAccessException,
          IllegalArgumentException, InvocationTargetException {
    thrown.expect(NotAQueryFactoryException.class);
    thrown.expectMessage(factoryErrorMessage(
        NotPropertiesFactory.class.getSimpleName(),
        notProperties(long.class, int.class)));
    $.addQuery(MyQuery.class, new NotPropertiesFactory());
  }

  /**
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   */
  @Test
  public final void
      factoryWitUnaddedPropertyTypesShouldThrowNotAQueryFactoryException()
          throws InstantiationException, IllegalAccessException,
          IllegalArgumentException, InvocationTargetException {
    thrown.expect(MissingPropertitesException.class);
    thrown.expectMessage(factoryPropertyMissingMessage(
        MySuperQueryFactory.class, MySuperProperty.class, MyProperty.class));
    $.addQuery(MyQuery.class, new MySuperQueryFactory(0));
  }

  private String factoryPropertyMissingMessage(final Class<?> factory,
      final Class<?>... properties) {
    final StringBuilder builder =
        new StringBuilder("The factory ").append(factory.getSimpleName())
            .append(" missing the properties: \n");
    for (final Class<?> parameterType : properties)
      builder.append("\t- " + parameterType.getSimpleName() + "\n");
    return builder.toString();
  }

  private String notProperties(final Class<?>... types) {
    final StringBuilder builder = new StringBuilder(":\n");
    for (final Class<?> paramterType : types)
      builder.append("\t-The parameter " + paramterType.getSimpleName()
          + " is not a property.\n");
    return builder.toString();
  }

  private String
      factoryErrorMessage(final String simpleName, final String cause) {
    return simpleName + " is not a legal "
        + TwitterQueryFactory.class.getSimpleName() + " because " + cause;
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
        + "\n\t(try to add a " + QuerySetup.class.getSimpleName()
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

  private static class ZeroPublicConstructorsMock implements TwitterQuery {
    private ZeroPublicConstructorsMock() {

    }
  }

  private static class OneConstructorsMock implements TwitterQuery {
    @QuerySetup
    public OneConstructorsMock() {
    }
  }

  // read with reflection
  @SuppressWarnings("unused")
  private static class TwoConstructorsMock implements TwitterQuery, Property {
    public TwoConstructorsMock() {
    }

    public TwoConstructorsMock(final List<Retweet> res1,
        final List<Retweet> res2) {

    }
  }

  private static class OneConstructorsAnnotatedMock implements TwitterQuery {
    // read with reflection
    @SuppressWarnings("unused")
    public OneConstructorsAnnotatedMock() {
    }

    @QuerySetup
    public OneConstructorsAnnotatedMock(final OneConstructorsMock mock) {

    }
  }

  private static class TwoConstructorsAnnotatedMock implements TwitterQuery {
    @QuerySetup
    public TwoConstructorsAnnotatedMock() {
    }

    @QuerySetup
    public TwoConstructorsAnnotatedMock(final OneConstructorsMock mock) {
    }
  }

  private static class NeedSupportedProperty implements TwitterQuery {
    public final int value;

    @QuerySetup
    public NeedSupportedProperty(final SupportedProperty a) {
      value = a.val.val;
    }
  }

  private static class SupportedProperty implements Property {
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

  private static class NotSupportedPropertyService implements TwitterQuery {
    @QuerySetup
    public NotSupportedPropertyService(final ZeroPublicConstructorsMock mock) {

    }
  }

  private static class NeedAbstractPropertyService implements TwitterQuery {
    @QuerySetup
    public NeedAbstractPropertyService(final AbstractProperty a) {
    }
  }

  private static class NeedInterfacePropertyService implements TwitterQuery {
    @QuerySetup
    public NeedInterfacePropertyService(final InterfaceProperty a) {
    }
  }

  private static class NeedPremitivePropertyService implements TwitterQuery {
    @SuppressWarnings("unused")
    public NeedPremitivePropertyService(final byte a) {
    }
  }

  private static abstract class AbstractProperty {
  }

  private static abstract class AbstractService implements TwitterQuery {

    @QuerySetup
    public AbstractService() {
    }
  }

  private static interface InterfaceProperty {
  }

  private static class SelfCircleService implements TwitterQuery {
    @QuerySetup
    public SelfCircleService(final SelfCircleService a) {
    }
  }

  private static class CircleHeadService implements TwitterQuery {
    @QuerySetup
    public CircleHeadService(final CircleFirstProperty a) {
    }
  }

  private static class CircleFirstProperty implements TwitterQuery {
    @QuerySetup
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

  private static class MultipuleMissingCausesClass implements TwitterQuery {
    // read with reflection
    @QuerySetup
    public MultipuleMissingCausesClass(final CircleFirstProperty a1,
        final char a2, final InterfaceProperty a3,
        final NotSupportedPropertyService a4) {
    }
  }

  private static class MyProperty implements Property {

    private final String string;
    private final String string2;

    public MyProperty(final String string, final String string2) {
      this.string = string;
      this.string2 = string2;
    }

    @Override
    public String toString() {
      return string + string2;
    }

  }

  private static class MySuperProperty extends MyProperty {

    private final int val;

    public MySuperProperty(final String string, final String string2,
        final int val) {
      super(string, string2);
      this.val = val;
    }

    @Override
    public String toString() {
      return super.toString() + val;
    }

  }

  private static class MySuperPropertyFactory implements
      PropertyFactory<MySuperProperty> {

    private final int val;

    public MySuperPropertyFactory(final int i) {
      val = i;
    }

    @Override
    public MySuperProperty get(final List<BaseTweet> baseTweets,
        final List<Retweet> retweets) {
      return new MySuperProperty(baseTweets.toString(), retweets.toString(),
          val);
    }

  }

  private static class MyQuery implements TwitterQuery {

    public final MyProperty pr;

    public MyQuery(final MyProperty pr1) {
      pr = pr1;
    }

    @Override
    public String toString() {
      return pr.toString();
    }

  }

  private static class MySuperQuery extends MyQuery {

    public MySuperProperty spr;
    public int val;

    public MySuperQuery(final MySuperProperty pr, final MyProperty pr1,
        final int val) {
      super(pr1);
      spr = pr;
      this.val = val;
    }

  }

  private static class MySuperQueryFactory implements
      TwitterQueryFactory<MySuperQuery> {
    private final int val;

    public MySuperQueryFactory(final int i) {
      val = i;
    }

    // read with reflection
    @SuppressWarnings("unused")
    public MySuperQuery get(final MySuperProperty spr, final MyProperty pr) {
      return new MySuperQuery(spr, pr, val);
    }
  }

  private static class NoGetFactory implements TwitterQueryFactory<MyQuery> {

    // read with reflection
    @SuppressWarnings("unused")
    public MySuperQuery ret(final MySuperProperty spr, final MyProperty pr) {
      return new MySuperQuery(spr, pr, 0);
    }
  }

  private static class TwoGetFactory implements TwitterQueryFactory<MyQuery> {

    // read with reflection
    @SuppressWarnings("unused")
    public MySuperQuery get(final MySuperProperty spr, final MyProperty pr) {
      return new MySuperQuery(spr, pr, 0);
    }

    // read with reflection
    @SuppressWarnings("unused")
    public MySuperQuery get(final MySuperProperty spr) {
      return new MySuperQuery(spr, spr, 0);
    }
  }

  private static class NotPropertiesFactory implements
      TwitterQueryFactory<MyQuery> {

    // read with reflection
    @SuppressWarnings("unused")
    public MySuperQuery get(final long r, final MySuperProperty spr,
        final int pr) {
      return new MySuperQuery(spr, spr, 0);
    }

  }

}

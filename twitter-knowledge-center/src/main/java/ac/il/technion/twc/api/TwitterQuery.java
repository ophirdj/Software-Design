package ac.il.technion.twc.api;

/**
 * A query for {@link TwitterDataCenterBuilder#registerQuery(Class)}.
 * 
 * 
 * {@link TwitterQuery} must have only one constructor or a constructor annotated
 * with {@link ServiceSetup}.
 * 
 * <br>
 * In addition, any type the constructor required must be createable. That mean
 * the type must not be a primitive and also be either (check in the following
 * order): <br>
 * - a property for which a property builder was register <br>
 * <b>or</b><br>
 * - fulfill All the following: <br>
 * (1) be a concrete class <br>
 * (2) have either a single public constructor or one public constructor
 * annotated with {@link ServiceSetup} <br>
 * (3) any type required by the constructor in (2) should also be createable
 * 
 * 
 * 
 * 
 * @author Ziv Ronen
 * @date 31.05.2014
 * @mail akarks@gmail.com
 * 
 */
public interface TwitterQuery {

}

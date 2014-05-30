package ac.il.technion.twc.impl.services.tagpopularity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import ac.il.technion.twc.impl.services.lifetime.TweetToLifeTime.UndefinedTimeException;

/**
 * Test for {@link TagToPopularity}
 * 
 * @author Ziv Ronen
 * @date 28.05.2014
 * @mail akarks@gmail.com
 * 
 * @version 2.0
 * @since 2.0
 */
public class TagToPopularityTest {

	/**
	 * Test method for {@link TagToPopularity#TagToPopularity()}
	 * 
	 * @throws UndefinedTimeException
	 */
	@Test
	public void emptyLifeTimeShouldReturnAllZeros()
			throws UndefinedTimeException {
		assertEquals(0, new TagToPopularity().getPopularityByHashtag("a"));
	}

	@Test
	public final void test() {
		fail("Not yet implemented"); // TODO
	}

}

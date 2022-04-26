package org.jfree.data.test;

import static org.junit.Assert.*;

import org.jfree.data.Range;
import org.jfree.data.RangeType;
import org.junit.*;

public class RangeTests {

	private Range nonNanExampleRange;
	private Range NanExampleRange;
	private Range lbNanExampleRange;
	private Range ubNanExampleRange;

	private Range exampleRange;
	private Range expectedRangeWithPosVal;
	private Range expectedRangeWithNegVal;
	private double posNewValue;
	private double negNewValue;
	private double existingValue;

	private Range exampleRange2;

	private Range baseRange;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("The testing of the class Range has been started.");
	}

	@Before
	public void setUp() throws Exception {
		nonNanExampleRange = new Range(-1, 1);
		NanExampleRange = new Range(Float.NaN, Float.NaN);
		lbNanExampleRange = new Range(Float.NaN, 1);
		ubNanExampleRange = new Range(-1, Float.NaN);

		exampleRange = new Range(0-1, 0+1);
		expectedRangeWithPosVal = new Range(-1.0, 2.0);
		expectedRangeWithNegVal = new Range(-2.0, 1.0);
		posNewValue = 2;
		negNewValue = -2;
		existingValue = 0;

		exampleRange2 = new Range(-2, 1);

		baseRange = new Range(-1, 1);
	}

	/***************
	 * Intersection
	 ***************/

	@Test
	public void rangeLowerBoundIntersectionShouldBeTrue() {
		// (####*****)
		// (*********####)
		// <-----2----1----0----1----2------>
		assertTrue("The lower bound of new range intersects with example range, hence value should be true",
				exampleRange2.intersects(0, 2));
	}

	@Test
	public void rangeUpperBoundIntersectionShouldBeTrue() {
		// (*****#########)
		// (#########*****)
		// <-3----2----1----0----1----2------>
		assertTrue("The upper bound of new range intersects with example range, hence value should be true",
				exampleRange2.intersects(-3, 0));
	}

	@Test
	public void rangeIntersectionShouldBeFalse1() {
		// (*****)
		// (**************)
		// <-3----2----1----0----1----2----3----4>
		assertFalse("The ranges do not intersect, hence value should be false", exampleRange2.intersects(3, 4));
	}

	@Test
	public void rangeIntersectionShouldBeFalse2() {
		assertFalse("The ranges do not intersect, hence value should be false", exampleRange2.intersects(-4, -3));
	}

	@Test
	public void rangeIntersectionShouldBeTrue1() {
		assertTrue("The new range completely falls within example range, hence value should be true",
				exampleRange2.intersects(-1, 0));
	}

	@Test
	public void rangeIntersectionShouldBeTrue2() {
		assertTrue("The example range completely falls within new range, hence value should be true",
				exampleRange2.intersects(-3, 4));
	}

	@Test
	public void rangeLowerBoundEqualShouldBeTrue() {
		assertTrue("The lower bound of new range is equal to that of example range, hence value should be true",
				exampleRange2.intersects(-2, 0));
	}

	@Test
	public void rangeUpperBoundEqualShouldBeTrue() {
		assertTrue("The Upper bound of new range is equal to that of example range, hence value should be true",
				exampleRange2.intersects(0, 1));
	}

	@Test
	public void rangeWithLBGreaterThanUB() {
		assertFalse("The example range's LB is greater than UB, hence value should be false",
				exampleRange2.intersects(0, -1));
	}

	/*****************
	 * ExpandToInclude
	 *****************/

	@Test
	public void expandedRangeIncludingTwo() {
		assertEquals("The range -1 and 1 should be -1 and 2 when including 2",
				Range.expandToInclude(exampleRange, posNewValue), expectedRangeWithPosVal);
	}

	@Test
	public void expandedRangeIncludingNegativeTwo() {
		assertEquals("The range -1 and 1 should be -2 and 1 when including -2",
				Range.expandToInclude(exampleRange, negNewValue), expectedRangeWithNegVal);
	}

	@Test
	public void expandedRangeIncludingExistingValueZero() {
		assertEquals("The range -1 and 1 should same when including 0",
				Range.expandToInclude(exampleRange, existingValue), exampleRange);
	}

	@Test
	public void expandingNullRange() {
		assertEquals("Should return null", Range.expandToInclude(null, 2), new Range(2, 2));
	}

	/***************
	 * IsNaNRange
	 **************/

	@Test
	public void noNaNValuesInRange() {
		assertFalse("The example range does not have any NaN values, should return false",
				nonNanExampleRange.isNaNRange());
	}

	@Test
	public void bothNaNValuesInRange() {
		assertTrue("The range has both NaN values in upper and lower bounds, should return true",
				NanExampleRange.isNaNRange());
	}

	@Test
	public void lowerBoundNaNValueInRange() {
		assertFalse("The range has one NaN value in lower bound, should return false", lbNanExampleRange.isNaNRange());
	}

	@Test
	public void upperBoundNaNValueInRange() {
		assertFalse("The range has one NaN value in upper bound, should return false", ubNanExampleRange.isNaNRange());
	}

	/*********************
	 * CombineIgnoringNaN
	 *********************/

	@Test
	public void shouldIgnoreLowerBoundNaNInRange1() {
		assertEquals("The new range should ignore Nan in lower bound of 1st range and use that of the 2nd range",
				new Range(0, 4), Range.combineIgnoringNaN(new Range(Double.NaN, -1), new Range(0, 4)));
	}

	@Test
	public void shouldIgnoreUpperBoundNaNInRange1() {
		assertEquals("The new range should ignore Nan in upper bound of 1st range and use that of the 2nd range",
				new Range(-1, 4), Range.combineIgnoringNaN(new Range(-1, Double.NaN), new Range(0, 4)));
	}

	@Test
	public void shouldIgnoreLowerBoundNaNInRange2() {
		assertEquals("The new range should ignore Nan in lower bound of 2nd range and use that of the 1st range",
				new Range(-1, 5), Range.combineIgnoringNaN(new Range(-1, 4), new Range(Double.NaN, 5)));
	}

	@Test
	public void shouldIgnoreUpperBoundNaNInRange2() {
		assertEquals("The new range should ignore Nan in upper bound of 2nd range and use that of the 1st range",
				new Range(-2, 4), Range.combineIgnoringNaN(new Range(-1, 4), new Range(-2, Double.NaN)));
	}

	@Test
	public void shouldIgnoreNaNInRange2() {
		assertEquals("The new range should ignore 2nd range completely", new Range(-1, 4),
				Range.combineIgnoringNaN(new Range(-1, 4), new Range(Double.NaN, Double.NaN)));
	}

	@Test
	public void shouldIgnoreNaNInRange1() {
		assertEquals("The new range should ignore 1st range completely", new Range(-1, 4),
				Range.combineIgnoringNaN(new Range(Double.NaN, Double.NaN), new Range(-1, 4)));
	}

	@Test
	public void shouldReturnNullAsResult() {
		assertEquals("The new range has no values, because all bounds are NaN!", null,
				Range.combineIgnoringNaN(new Range(Double.NaN, Double.NaN), new Range(Double.NaN, Double.NaN)));
	}

	@Test
	public void range1NullWithRange2NaN() {
		assertEquals("Since range1 is NULL and range2 has NaN values, should return null", null,
				Range.combineIgnoringNaN(null, new Range(Double.NaN, Double.NaN)));
	}

	@Test
	public void range1NullWithRange2NonNaN() {
		assertEquals("Since range1 is NULL and range2 has non-NaN values, should return range2", new Range(1, 2),
				Range.combineIgnoringNaN(null, new Range(1, 2)));
	}

	@Test
	public void range2NullWithRange1NaN() {
		assertEquals("Since range2 is NULL and range1 has NaN values, should return null", null,
				Range.combineIgnoringNaN(new Range(Double.NaN, Double.NaN), null));
	}

	@Test
	public void range2NullWithRange1NonNaN() {
		assertEquals("Since range2 is NULL and range1 has non-NaN values, should return range1", new Range(1, 2),
				Range.combineIgnoringNaN(new Range(1, 2), null));
	}

	@Test
	public void bothRangesNull() {
		assertEquals("Since both ranges are null, should return null", null, Range.combineIgnoringNaN(null, null));
	}

	@Test
	public void bothRangesNaN() {
		Range result = Range.combineIgnoringNaN(new Range(Double.NaN, Double.NaN), new Range(Double.NaN, Double.NaN));
		if(result != null) {
			fail("Since both ranges have NaNs as both bound values, should return null");
		}
	}

	@Test
	public void lowerBoundNaN() {
		Range result = Range.combineIgnoringNaN(new Range(Double.NaN, 2), new Range(Double.NaN, 3));
		if(Double.isNaN(result.getLowerBound()) == false) {
			fail("Since both ranges have NaNs as in LBs, should return new range with NaN in LB and max of UBs");
		} else if (result.getUpperBound() != 3) {
			fail("The upper bond must be 3.");
		}
	}

	@Test
	public void upperBoundNaN() {
		Range result = Range.combineIgnoringNaN(new Range(2, Float.NaN), new Range(3, Float.NaN));
		if(result.getLowerBound() != 2) {
			fail("The lower bounds must be 2.");
		} else if(!Double.isNaN(result.getUpperBound())) {
			fail("Since both ranges have NaNs as in UBs, should return new range with NaN in UB and min of LBs");
		}
	}
	

	/*********
	 * Shift
	 *********/

	@Test
	// The range's lower and upper bounds should be added by 2 with lower bound crossing zero
	public void shiftTowardsPositiveScaleByCrossingZero() {
		// (-1,+1) --Shift 2 to right --> (+1,+3)
		Range range = Range.shift(baseRange, 2, true);
		if(range.getLowerBound() != 1) {
			fail("The lower bounds must be 1.");
		} else if (range.getUpperBound() != 3) {
			fail("The upper bond must be 3.");
		}
	}

	@Test
	//The range's lower and upper bounds should be subtracted by 2 with upper bound crossing zero
	public void shiftTowardsNegativeScaleByCrossingZero() {
		// (-1,+1) --Shift 2 to left --> (-3,-1)
		Range range = Range.shift(baseRange, -2, true);
		if(range.getLowerBound() != -3) {
			fail("The lower bounds must be -3.");
		} else if (range.getUpperBound() != -1) {
			fail("The upper bond must be -1.");
		}
	}

	@Test
	//The range's lower and upper bounds should be added by 2 without crossing zero, so lower bound's value should be zero
	public void shiftTowardsPositiveScaleWithoutCrossingZero() {
		// (-1,+1) --Shift 2 to right, Not passing 0 --> (0,+3)
		Range range = Range.shift(baseRange, 2, false);
		if(range.getLowerBound() != 0) {
			fail("The lower bounds must be 0.");
		} else if (range.getUpperBound() != 3) {
			fail("The upper bond must be 3.");
		}
	}

	@Test
	//The range's lower and upper bounds should be subtracted by 2 without crossing zero, so upper bound's value should be zero
	public void shiftTowardsNegativeScaleWithoutCrossingZero() {
		// (-1,+1) --Shift 2 to left, Not passing 0 --> (-3.0,0)
		Range range = Range.shift(baseRange, -2, false);
		if(range.getLowerBound() != -3) {
			fail("The lower bounds must be -3.");
		} else if (range.getUpperBound() != 0) {
			fail("The upper bond must be 0.");
		}
	}

	@Test
	//Shift value is zero, so same range as result
	public void shiftValueIsZero() {
		// (-1,+1) --Shift 0 to left, Not passing 0 --> (-1,+1)
		Range range = Range.shift(baseRange, 0, false);
		if(range.getLowerBound() != -1) {
			fail("The lower bounds must be -1.");
		} else if (range.getUpperBound() != 1) {
			fail("The upper bond must be 1.");
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

}

package org.jfree.data.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.ThreadLocalRandom;
import org.jfree.data.DataUtilities;
import org.jfree.data.KeyedValues;
import org.jfree.data.Values2D;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataUtilitiesTests {

	private Mockery mockingContext;
	private Values2D values2D;

	private Mockery mockingContextKV;
	private KeyedValues keyValues;

	private double[][] doubleEmpty;
	private double[][] doubleSingle;
	private double[][] doubleRowBoundaries;
	private double[][] doubleColumnBoundaries;
	private double[][] doubleMxNBoundaries;

	private double[] doubleSingleArrayBoundaries;
	private double[] doubleSingleArrayEmpty;
	private double[] doubleSingleArray;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("The test of the functions in the class DataUtilities has been started.");
	}

	@Before
	public void setUp() throws Exception {
		// for 2D arrays
		mockingContext = new Mockery();
		values2D = mockingContext.mock(Values2D.class);

		// for getCumulativePercentages
		mockingContextKV = new Mockery();
		keyValues = mockingContextKV.mock(KeyedValues.class);

		// Empty
		double[][] d0 = {};
		doubleEmpty = d0;

		// Generates a random single value array
		double d = ThreadLocalRandom.current().nextDouble();
		double[][] d1 = { { d } };
		doubleSingle = d1;

		// Generates a row value array
		double[][] d2 = { { Double.MAX_VALUE, 0.0d, Double.MIN_NORMAL, Double.MIN_VALUE, Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY, Double.MAX_EXPONENT, Double.MIN_EXPONENT, 1.23456e300d, -1.23456e-300d,
				1e1d } };
		doubleRowBoundaries = d2;

		// Generates a column value array
		double[][] d3 = { { Double.MAX_VALUE }, { 0.0d }, { Double.MIN_NORMAL }, { Double.MIN_VALUE },
				{ Double.NEGATIVE_INFINITY }, { Double.POSITIVE_INFINITY }, { Double.MAX_EXPONENT },
				{ Double.MIN_EXPONENT }, { 1.23456e300d }, { -1.23456e-300d }, { 1e1d } };
		doubleColumnBoundaries = d3;

		// Generates a 3x4 row value array
		double[][] d4 = { { Double.MAX_VALUE, 0.0d, Double.MIN_NORMAL },
				{ Double.MIN_VALUE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY },
				{ Double.MAX_EXPONENT, Double.MIN_EXPONENT, 1.23456e300d }, { -1.23456e-300d, 1e1d, d } };
		doubleMxNBoundaries = d4;

		// Single array
		double[] ds1 = { Double.MAX_VALUE, 0.0d, Double.MIN_NORMAL, Double.MIN_VALUE, Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY, Double.MAX_EXPONENT, Double.MIN_EXPONENT, 1.23456e300d, -1.23456e-300d,
				1e1d };
		doubleSingleArrayBoundaries = ds1;

		// Empty
		double[] ds2 = {};
		doubleSingleArrayEmpty = ds2;

		// Generates a random single value array
		double[] ds3 = { d };
		doubleSingleArray = ds3;

	}

	/*************************
	 * calculateColumnTotalTest
	 *************************/

	@Test
	public void calculateColumnTotalTestForTwoPositiveValues() {
		mockingContext.checking(new Expectations() {
			{
				one(values2D).getRowCount();
				will(returnValue(2));
				one(values2D).getValue(0, 0); // int row, int column
				will(returnValue(7.6d));
				one(values2D).getValue(1, 0); // int row, int column
				will(returnValue(2.4d));
				// 0
				// --------
				// 0 | 7.6
				// 1 | 2.4
			}
		});

		double result = DataUtilities.calculateColumnTotal(values2D, 0);
		assertEquals("Two positive double values passed and expect 10.0d as result", result, 10.0, .000000001d);
	}

	@Test
	public void calculateColumnTotalTestForTwoNegativeValues() {
		mockingContext.checking(new Expectations() {
			{
				one(values2D).getRowCount();
				will(returnValue(2));
				one(values2D).getValue(0, 1); // int row, int column
				will(returnValue(-7.6));
				one(values2D).getValue(1, 1); // int row, int column
				will(returnValue(-2.4));
			}
		});

		double result = DataUtilities.calculateColumnTotal(values2D, 1);
		assertEquals("Two negative double values passed and expect -10.0d as result", result, -10.0, .000000001d);

	}

	@Test
	public void calculateColumnTotalTestForZeroColumn() {
		Mockery context = new Mockery();
		final Values2D values = context.mock(Values2D.class);
		context.checking(new Expectations() {
			{
				one(values).getRowCount();
				will(returnValue(2));
				one(values).getValue(0, 0);
				will(returnValue(-10.0));
				one(values).getValue(1, 0);
				will(returnValue(-5.5));
			}
		});

		double result = DataUtilities.calculateColumnTotal(values, 0);
		assertEquals(result, -15.5, .000000001d);

	}

	@Test
	public void calculateColumnTotalTestForTwoNegativePositiveValues() {
		mockingContext.checking(new Expectations() {
			{
				one(values2D).getRowCount();
				will(returnValue(2));
				one(values2D).getValue(0, 0); // int row, int column
				will(returnValue(-7.5));
				one(values2D).getValue(1, 0); // int row, int column
				will(returnValue(2.5));
			}
		});

		double result = DataUtilities.calculateColumnTotal(values2D, 0);
		assertEquals("Two negative and positive double values passed and expect 5.0d as result", result, -5.0,
				.000000001d);

	}

	@Test
	public void calculateColumnTotalTestForTwoPositiveNegativeValues() {
		mockingContext.checking(new Expectations() {
			{
				one(values2D).getRowCount();
				will(returnValue(2));
				one(values2D).getValue(0, 0); // int row, int column
				will(returnValue(7.5));
				one(values2D).getValue(1, 0); // int row, int column
				will(returnValue(-2.5));
			}
		});

		double result = DataUtilities.calculateColumnTotal(values2D, 0);
		assertEquals("Two negative and positive double values passed and expect 5.0d as result", result, 5.0,
				.000000001d);

	}

	@Test
	public void calculateColumnTotalTestForTwoOppositePolaritySameMagnitudeValues() {
		mockingContext.checking(new Expectations() {
			{
				one(values2D).getRowCount();
				will(returnValue(2));
				one(values2D).getValue(0, 0); // int row, int column
				will(returnValue(7.5));
				one(values2D).getValue(1, 0); // int row, int column
				will(returnValue(-7.5));
			}
		});

		double result = DataUtilities.calculateColumnTotal(values2D, 0);
		assertEquals("Two opposite polarity same magnitude double values passed and expect 0.0d as result", result, 0.0,
				.000000001d);

	}

	@Test
	public void calculateColumnTotalTestForTwoZeroValues() {
		mockingContext.checking(new Expectations() {
			{
				one(values2D).getRowCount();
				will(returnValue(2));
				one(values2D).getValue(0, 0); // int row, int column
				will(returnValue(0.0));
				one(values2D).getValue(1, 0); // int row, int column
				will(returnValue(0.0));
			}
		});

		double result = DataUtilities.calculateColumnTotal(values2D, 0);
		assertEquals(result, 0.0, .000000001d);

	}

	@Test
	public void calculateColumnTotalTestForSingleValue() {
		mockingContext.checking(new Expectations() {
			{
				one(values2D).getRowCount();
				will(returnValue(1));
				one(values2D).getValue(0, 0); // int row, int column
				will(returnValue(50.36598));
			}
		});

		double result = DataUtilities.calculateColumnTotal(values2D, 0);
		assertEquals(result, 50.36598, .000000000d);

	}

	@Test
	public void calculateColumnTotalTestForNullValue() {
		mockingContext.checking(new Expectations() {
			{
				one(values2D).getRowCount();
				will(returnValue(1));
				one(values2D).getValue(0, 0); // int row, int column
				will(returnValue(null));
			}
		});

		double result = DataUtilities.calculateColumnTotal(values2D, 0);
		assertEquals(result, 0.0d, .000000000d);

	}

	@Test
	public void calculateColumnTotalTestForNullAndNotNull() {
		Mockery context = new Mockery();
		final Values2D values = context.mock(Values2D.class);
		context.checking(new Expectations() {
			{
				one(values).getRowCount();
				will(returnValue(3));
				one(values).getValue(0, 1);
				will(returnValue(null));
				one(values).getValue(1, 1);
				will(returnValue(2.2));
				one(values).getValue(2, 1);
				will(returnValue(null));
			}
		});

		double result = DataUtilities.calculateColumnTotal(values, 1);
		assertEquals(2.2, result, .000000001d);

	}

	@Test
	public void calculateColumnTotalTestForEmptyValues() {
		mockingContext.checking(new Expectations() {
			{
				one(values2D).getRowCount();
				will(returnValue(0));
			}
		});

		double result = DataUtilities.calculateColumnTotal(values2D, 0);
		assertEquals(result, 0, .000000001d);

	}

	@Test
	public void calculateColumnTotalTestForPrecision() {
		mockingContext.checking(new Expectations() {
			{
				one(values2D).getRowCount();
				will(returnValue(2));
				one(values2D).getValue(0, 0); // int row, int column
				will(returnValue(5.6));
				one(values2D).getValue(1, 0); // int row, int column
				will(returnValue(5.8));
			}
		});

		double result = DataUtilities.calculateColumnTotal(values2D, 0);
		assertEquals(result, 11.399999999999999d, 0d);

	}

	@Test
	public void CalculateColumnTotalTestForNegativeRow() {
		Mockery context = new Mockery();
		final Values2D values = context.mock(Values2D.class);
		context.checking(new Expectations() {
			{
				one(values).getRowCount();
				will(returnValue(2));
				one(values).getValue(0, -1);
				will(throwException(new IndexOutOfBoundsException()));
				one(values).getValue(1, -1);
				will(throwException(new IndexOutOfBoundsException()));
			}
		});

		try {
			double result = DataUtilities.calculateColumnTotal(values, -1);
		} catch (IndexOutOfBoundsException e) {
		}
	}

	@Test
	public void calculateColumnTotalTestForRowOutOfRangeRow() {
		Mockery context = new Mockery();
		final Values2D values = context.mock(Values2D.class);
		context.checking(new Expectations() {
			{
				one(values).getRowCount();
				will(returnValue(2));
				one(values).getValue(0, 3);
				will(throwException(new IndexOutOfBoundsException()));
				one(values).getValue(1, 3);
				will(throwException(new IndexOutOfBoundsException()));
			}
		});

		try {
			Double result = DataUtilities.calculateColumnTotal(values, 3);
		} catch (IndexOutOfBoundsException e) {
		}
	}

	/***************************
	 * calculateRowTotal
	 ***************************/

	@Test
	public void calculateRowTotalTestForTwoPositiveValues() {
		mockingContext.checking(new Expectations() {
			{
				one(values2D).getColumnCount();
				will(returnValue(2));
				one(values2D).getValue(0, 0); // int row, int column
				will(returnValue(7.6));
				one(values2D).getValue(0, 1); // int row, int column
				will(returnValue(2.4));
				// 0 1
				// ------------------
				// 0 | 7.6 2.4
			}
		});
		double result = DataUtilities.calculateRowTotal(values2D, 0);
		assertEquals(result, 10.0, .000000001d);

	}

	@Test
	public void calculateRowTotalTestForTwoNegativeValues() {
		mockingContext.checking(new Expectations() {
			{
				one(values2D).getColumnCount();
				will(returnValue(2));
				one(values2D).getValue(0, 0); // int row, int column
				will(returnValue(-7.6));
				one(values2D).getValue(0, 1); // int row, int column
				will(returnValue(-2.4));
			}
		});
		double result = DataUtilities.calculateRowTotal(values2D, 0);
		assertEquals(result, -10.0, .000000001d);
	}

	@Test
	public void calculateRowTotalTestForTwoNegativePositiveValues() {
		mockingContext.checking(new Expectations() {
			{
				one(values2D).getColumnCount();
				will(returnValue(2));
				one(values2D).getValue(0, 0); // int row, int column
				will(returnValue(-7.5));
				one(values2D).getValue(0, 1); // int row, int column
				will(returnValue(2.5));
			}
		});
		double result = DataUtilities.calculateRowTotal(values2D, 0);
		assertEquals(result, -5.0, .000000001d);

	}

	@Test
	public void calculateRowTotalTestForTwoPositiveNegativeValues() {
		mockingContext.checking(new Expectations() {
			{
				one(values2D).getColumnCount();
				will(returnValue(2));
				one(values2D).getValue(0, 0); // int row, int column
				will(returnValue(7.5));
				one(values2D).getValue(0, 1); // int row, int column
				will(returnValue(-2.5));
			}
		});

		// Returns the total of the values in one row of the supplied data table.
		double result = DataUtilities.calculateRowTotal(values2D, 0);
		assertEquals(result, 5.0, .000000001d);

	}

	@Test
	public void calculateRowTotalTestForTwoOppositePolaritySameMagnitudeValues() {
		mockingContext.checking(new Expectations() {
			{
				one(values2D).getColumnCount();
				will(returnValue(2));
				one(values2D).getValue(0, 0); // int row, int column
				will(returnValue(7.5));
				one(values2D).getValue(0, 1); // int row, int column
				will(returnValue(-7.5));
			}
		});

		// Returns the total of the values in one row of the supplied data table.
		double result = DataUtilities.calculateRowTotal(values2D, 0);
		assertEquals(result, 0.0, .000000001d);

	}

	@Test
	public void calculateRowTotalTestForTwoZeroValues() {
		mockingContext.checking(new Expectations() {
			{
				one(values2D).getColumnCount();
				will(returnValue(2));
				one(values2D).getValue(0, 0); // int row, int column
				will(returnValue(0.0));
				one(values2D).getValue(0, 1); // int row, int column
				will(returnValue(0.0));
			}
		});

		// Returns the total of the values in one row of the supplied data table.
		double result = DataUtilities.calculateRowTotal(values2D, 0);
		assertEquals(result, 0.0, .000000001d);

	}

	@Test
	public void calculateRowTotalTestForSingleValue() {
		mockingContext.checking(new Expectations() {
			{
				one(values2D).getColumnCount();
				will(returnValue(1));
				one(values2D).getValue(0, 0); // int row, int column
				will(returnValue(50.36598));
			}
		});

		// Returns the total of the values in one row of the supplied data table.
		double result = DataUtilities.calculateRowTotal(values2D, 0);
		assertEquals(result, 50.36598, .000000000d);

	}

	@Test
	public void calculateRowTotalTestForNullValue() {
		mockingContext.checking(new Expectations() {
			{
				one(values2D).getColumnCount();
				will(returnValue(1));
				one(values2D).getValue(0, 0); // int row, int column
				will(returnValue(null));
			}
		});

		// Returns the total of the values in one row of the supplied data table.
		double result = DataUtilities.calculateRowTotal(values2D, 0);
		assertEquals(result, 0.0d, .000000000d);
	}

	@Test
	public void CalculateRowTotalTestForNullandNotNull() {
		Mockery context = new Mockery();
		final Values2D values = context.mock(Values2D.class);
		context.checking(new Expectations() {
			{
				one(values).getColumnCount();
				will(returnValue(3));
				one(values).getValue(1, 0);
				will(returnValue(null));
				one(values).getValue(1, 1);
				will(returnValue(2.2));
				one(values).getValue(1, 2);
				will(returnValue(null));
			}
		});

		double result = DataUtilities.calculateRowTotal(values, 1);
		assertEquals(2.2, result, .000000001d);

	}

	@Test
	public void calculateRowTotalTestForEmptyValues() {
		mockingContext.checking(new Expectations() {
			{
				one(values2D).getColumnCount();
				will(returnValue(0));
			}
		});
		// Returns the total of the values in one row of the supplied data table.
		double result = DataUtilities.calculateRowTotal(values2D, 0);
		assertEquals(result, 0, .000000001d);

	}

	@Test
	public void calculateRowTotalTestForPrecision() {
		mockingContext.checking(new Expectations() {
			{
				one(values2D).getColumnCount();
				will(returnValue(2));
				one(values2D).getValue(0, 0); // int row, int column
				will(returnValue(5.6));
				one(values2D).getValue(0, 1); // int row, int column
				will(returnValue(5.8));
			}
		});

		// Returns the total of the values in one row of the supplied data table.
		double result = DataUtilities.calculateRowTotal(values2D, 0);
		assertEquals(result, 11.399999999999999d, 0d);

	}

	@Test
	public void calculateRowTotalTestForNegativeRow() {
		Mockery context = new Mockery();
		final Values2D values = context.mock(Values2D.class);
		context.checking(new Expectations() {
			{
				one(values).getColumnCount();
				will(returnValue(2));
				one(values).getValue(-1, 0);
				will(throwException(new IndexOutOfBoundsException()));
				one(values).getValue(-1, 1);
				will(throwException(new IndexOutOfBoundsException()));
			}
		});

		try {
			double result = DataUtilities.calculateRowTotal(values, -1);
		} catch (IndexOutOfBoundsException e) {
		}
	}

	@Test
	public void calculateRowTotalTestForRowOutOfRangeRow() {
		Mockery context = new Mockery();
		final Values2D values = context.mock(Values2D.class);
		context.checking(new Expectations() {
			{
				one(values).getColumnCount();
				will(returnValue(2));
				one(values).getValue(3, 0);
				will(throwException(new IndexOutOfBoundsException()));
				one(values).getValue(3, 1);
				will(throwException(new IndexOutOfBoundsException()));
			}
		});
		try {
			Double result = DataUtilities.calculateRowTotal(values, 3);
		} catch (IndexOutOfBoundsException e) {
		}
	}

	/**********************
	 * CreateNumberArray2D
	 **********************/

	@SuppressWarnings({ "deprecation" })
	@Test
	public void createNumberArray2DTestForEmptyDoubleArray() {
		// int, double, float
		// Integer, Double, Float : Number
		Double[][] doubleArr = dArray2DArray(doubleEmpty);
		Number[][] numberArr = doubleArr;
		Number[][] result = DataUtilities.createNumberArray2D(doubleEmpty);

		// Assert object by object
		assertArrayEquals(result, numberArr);

	}

	@Test
	public void createNumberArray2DTestForSingleDoubleArray() {

		Double[][] doubleArr = dArray2DArray(doubleSingle);
		Number[][] numberArr = doubleArr;
		Number[][] result = DataUtilities.createNumberArray2D(doubleSingle);

		// Assert object by object
		assertArrayEquals(result, numberArr);
	}

	@Test
	public void createNumberArray2DTestForSingleRowDoubleArray() {
		Double[][] doubleArr = dArray2DArray(doubleRowBoundaries);

		Number[][] numberArr = doubleArr;
		Number[][] result = DataUtilities.createNumberArray2D(doubleRowBoundaries);

		// Assert object by object
		assertArrayEquals(result, numberArr);

	}

	@Test
	public void createNumberArray2DTestForSingleColumnDoubleArray() {
		Double[][] doubleArr = dArray2DArray(doubleColumnBoundaries);

		Number[][] numberArr = doubleArr;
		Number[][] result = DataUtilities.createNumberArray2D(doubleColumnBoundaries);

		// Assert object by object
		assertArrayEquals(result, numberArr);
	}

	@Test
	public void createNumberArray2DTestForMxNDoubleArray() {
		Double[][] doubleArr = dArray2DArray(doubleMxNBoundaries);

		Number[][] numberArr = doubleArr;
		Number[][] result = DataUtilities.createNumberArray2D(doubleMxNBoundaries);

		// Assert object by object
		assertArrayEquals(numberArr, result);

	}

	@Test
	public void createNumberArray2DTestForNullArray() {

		double[][] input = null;

		try {
			Number[][] result = DataUtilities.createNumberArray2D(input);
		} catch (IllegalArgumentException e) {
		}
	}

	/********************
	 * CreateNumberArray
	 ********************/

	@Test
	public void createNumberArrayTestForMultiDoubleArray() {
		Double[] doubleArr = dArray2DArray(doubleSingleArrayBoundaries);

		Number[] numberArr = doubleArr;
		Number[] result = DataUtilities.createNumberArray(doubleSingleArrayBoundaries);

		// Assert object by object
		assertArrayEquals(result, numberArr);

	}

	@Test
	public void createNumberArrayTestForEmptyDoubleArray() {
		Double[] doubleArr = dArray2DArray(doubleSingleArrayEmpty);
		Number[] numberArr = doubleArr;
		Number[] result = DataUtilities.createNumberArray(doubleSingleArrayEmpty);

		// Assert object by object
		assertArrayEquals(result, numberArr);
	}

	@Test
	public void createNumberArrayTestForSingleDoubleArray() {

		Double[] doubleArr = dArray2DArray(doubleSingleArray);
		Number[] numberArr = doubleArr;
		Number[] result = DataUtilities.createNumberArray(doubleSingleArray);

		// Assert object by object
		assertArrayEquals(result, numberArr);
	}

	@Test
	public void createNumberArrayTestForNullArray() {

		double[] input = null;

		try {
			Number[] result = DataUtilities.createNumberArray(input);
		} catch (IllegalArgumentException e) {
		}
	}

	/***************************
	 * getCumulativePercentages
	 ***************************/

	@Test
	public void getCumulativePercentagesTestForFourPositiveValues() {

		// List: {2,2,2,2}
		// Cumulative Percentages: {2/8, 4/8, 6/8, 8/8}

		mockingContextKV.checking(new Expectations() {
			{
				allowing(keyValues).getItemCount();
				will(returnValue(4));
				// returning the values
				allowing(keyValues).getValue(0);
				will(returnValue(2));
				allowing(keyValues).getValue(1);
				will(returnValue(2));
				allowing(keyValues).getValue(2);
				will(returnValue(2));
				allowing(keyValues).getValue(3);
				will(returnValue(2));
				// converting indices to keys
				allowing(keyValues).getKey(0);
				will(returnValue(0));
				allowing(keyValues).getKey(1);
				will(returnValue(1));
				allowing(keyValues).getKey(2);
				will(returnValue(2));
				allowing(keyValues).getKey(3);
				will(returnValue(3));
			}
		});

		KeyedValues result = DataUtilities.getCumulativePercentages(keyValues);

		assertEquals(0.25, result.getValue(0));
		assertEquals(0.5, result.getValue(1));
		assertEquals(0.75, result.getValue(2));
		assertEquals(1.0, result.getValue(3));

	}

	@Test
	public void getCumulativePercentagesTestForSinglePositiveValues() {
		mockingContextKV.checking(new Expectations() {
			{
				allowing(keyValues).getItemCount();
				will(returnValue(1));
				allowing(keyValues).getValue(0);
				will(returnValue(2));
				allowing(keyValues).getKey(0);
				will(returnValue(0));
			}
		});

		KeyedValues result = DataUtilities.getCumulativePercentages(keyValues);
		assertEquals("If I pass only one value I expect 1.0 as Cumulative Percentage", 1.0, result.getValue(0));

	}

	@Test
	public void getCumulativePercentagesTestForSingleNullValues() {
		mockingContextKV.checking(new Expectations() {
			{
				allowing(keyValues).getItemCount();
				will(returnValue(1));
				allowing(keyValues).getValue(0);
				will(returnValue(null));
				allowing(keyValues).getKey(0);
				will(returnValue(0));
			}
		});

		KeyedValues result = DataUtilities.getCumulativePercentages(keyValues);
		assertEquals("If I pass only one null value I expect NaN as Cumulative Percentage", Double.NaN,
				result.getValue(0));

	}

	@Test
	public void getCumulativePercentagesTestForEmptyValues() {
		mockingContextKV.checking(new Expectations() {
			{
				allowing(keyValues).getItemCount();
				will(returnValue(0));
			}
		});

		KeyedValues result = DataUtilities.getCumulativePercentages(keyValues);
		assertEquals(0, result.getItemCount());

	}

	@Test
	public void getCumulativePercentagesTestForFirstThreeRowsAreZero() {
		mockingContextKV.checking(new Expectations() {
			{
				allowing(keyValues).getItemCount();
				will(returnValue(4));
				// {0,0,0,2}
				allowing(keyValues).getValue(0);
				will(returnValue(0));
				allowing(keyValues).getValue(1);
				will(returnValue(0));
				allowing(keyValues).getValue(2);
				will(returnValue(0));
				allowing(keyValues).getValue(3);
				will(returnValue(2));
				//
				allowing(keyValues).getKey(0);
				will(returnValue(0));
				allowing(keyValues).getKey(1);
				will(returnValue(1));
				allowing(keyValues).getKey(2);
				will(returnValue(2));
				allowing(keyValues).getKey(3);
				will(returnValue(3));
			}
		});
		KeyedValues result = DataUtilities.getCumulativePercentages(keyValues);
		// All must be zero expect the last one.
		assertEquals(0.0, result.getValue(0));
		assertEquals(0.0, result.getValue(1));
		assertEquals(0.0, result.getValue(2));
		assertEquals(1.0, result.getValue(3));
	}

	@Test
	public void getCumulativePercentagesTestForLastThreeRowsAreZero() {
		mockingContextKV.checking(new Expectations() {
			{
				allowing(keyValues).getItemCount();
				will(returnValue(4));
				//
				allowing(keyValues).getValue(0);
				will(returnValue(2));
				allowing(keyValues).getValue(1);
				will(returnValue(0));
				allowing(keyValues).getValue(2);
				will(returnValue(0));
				allowing(keyValues).getValue(3);
				will(returnValue(0));
				//
				allowing(keyValues).getKey(0);
				will(returnValue(0));
				allowing(keyValues).getKey(1);
				will(returnValue(1));
				allowing(keyValues).getKey(2);
				will(returnValue(2));
				allowing(keyValues).getKey(3);
				will(returnValue(3));
			}
		});

		KeyedValues result = DataUtilities.getCumulativePercentages(keyValues);
		// Expect all to be 1.0
		assertEquals(1.0, result.getValue(0));
		assertEquals(1.0, result.getValue(1));
		assertEquals(1.0, result.getValue(2));
		assertEquals(1.0, result.getValue(3));
	}

	@Test
	public void getCumulativePercentagesTestForAllRowsAreZero() {
		mockingContextKV.checking(new Expectations() {
			{
				allowing(keyValues).getItemCount();
				will(returnValue(4));
				//
				allowing(keyValues).getValue(0);
				will(returnValue(0));
				allowing(keyValues).getValue(1);
				will(returnValue(0));
				allowing(keyValues).getValue(2);
				will(returnValue(0));
				allowing(keyValues).getValue(3);
				will(returnValue(0));
				//
				allowing(keyValues).getKey(0);
				will(returnValue(0));
				allowing(keyValues).getKey(1);
				will(returnValue(1));
				allowing(keyValues).getKey(2);
				will(returnValue(2));
				allowing(keyValues).getKey(3);
				will(returnValue(3));
			}
		});

		KeyedValues result = DataUtilities.getCumulativePercentages(keyValues);
		assertEquals(0.0 / 0.0, result.getValue(0));
		assertEquals(0.0 / 0.0, result.getValue(1));
		assertEquals(0.0 / 0.0, result.getValue(2));
		assertEquals(0.0 / 0.0, result.getValue(3));
	}

	@Test
	public void cumulativePercentagesTestForNull() {
		try {
			KeyedValues result = DataUtilities.getCumulativePercentages(null);
		} catch (IllegalArgumentException e) {
			
		}
	}

	@After
	public void tearDown() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}

	// converts the a 2D double array to a 2D Double array
	private static Double[][] dArray2DArray(double[][] data) {
		Double[][] doubleArr = new Double[data.length][];
		for (int i = 0; i < data.length; i++) {
			doubleArr[i] = new Double[data[i].length];
			for (int j = 0; j < data[i].length; j++) {
				doubleArr[i][j] = new Double(data[i][j]);
			}
		}
		return doubleArr;
	}

	private static Double[] dArray2DArray(double[] data) {
		Double[] doubleArr = new Double[data.length];
		for (int i = 0; i < data.length; i++) {
			doubleArr[i] = new Double(data[i]);
		}
		return doubleArr;
	}

}

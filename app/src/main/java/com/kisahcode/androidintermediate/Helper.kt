package com.kisahcode.androidintermediate

import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Extension function to format a string representing a number into a human-readable format.
 *
 * The input string should represent a number (e.g., "1000").
 * The function formats this string into a localized number format, adding commas to separate thousands.
 * For example, "1000" becomes "1,000" in the US locale.
 *
 * @return The formatted number string.
 */
fun String.withNumberingFormat(): String {
    return NumberFormat.getNumberInstance().format(this.toDouble())
}

/**
 * Extension function to format a string representing a date into a human-readable date format.
 *
 * The input string should be in the format "dd/MM/yyyy" (e.g., "21/04/2024").
 * The function converts this string into a fully formatted date string based on the device's locale.
 * For example, in the US locale, the output might be "Wednesday, April 21, 2024".
 *
 * @return The formatted date string.
 */
fun String.withDateFormat(): String {
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    val date = format.parse(this) as Date
    return DateFormat.getDateInstance(DateFormat.FULL).format(date)
}

/**
 * Extension function to format a string representing a price into a human-readable currency format
 * based on the device's locale.
 *
 * The input string should represent a numeric price value (e.g., "1000").
 * The function converts this price into a formatted currency string based on the device's locale.
 * If the device locale is Spanish ("ES"), the price is converted to Euros and formatted accordingly.
 * If the device locale is Indonesian ("ID"), the price is converted to Indonesian Rupiah and formatted.
 * For other locales, the price is formatted in the default currency (US dollars).
 *
 * @return The formatted currency string.
 */
fun String.withCurrencyFormat(): String {
    // Exchange rates for Euro and Indonesian Rupiah against US Dollar.
    val rupiahExchangeRate  = 12495.95
    val euroExchangeRate = 0.88

    // Convert the price to its equivalent value in US Dollars.
    var priceOnDollar = this.toDouble() / rupiahExchangeRate

    // Get a NumberFormat instance for currency formatting.
    var mCurrencyFormat = NumberFormat.getCurrencyInstance()

    // Get the device's locale.
    val deviceLocale = Locale.getDefault().country

    // Determine the currency formatting based on the device's locale.
    when {
        deviceLocale.equals("ES") -> {
            // If the device locale is Spanish, convert the price to Euros.
            priceOnDollar *= euroExchangeRate
        }
        deviceLocale.equals("ID") -> {
            // If the device locale is Indonesian, convert the price to Indonesian Rupiah.
            priceOnDollar *= rupiahExchangeRate
        }
        else -> {
            // For other locales, use the default currency format (US dollars).
            mCurrencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
        }
    }

    // Format the price in the appropriate currency format.
    return mCurrencyFormat.format(priceOnDollar)
}
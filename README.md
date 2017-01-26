# JsonDataSearch

Note: The company names returned by the application are ficticious, as are the prices and product types in relation to the product name. The data was randomly generated, and I take no responsibility for anyone's medical conditions. Please do not rely on the data in this application for your health!

Some assumptions have been made:
- The application should ignore input parameters that it does not understand. If the requested doctype is not understood, an error will be displayed. For all other input, errors are silently ignored. This includes filtering and sorting.
- No specification to output logs, Logger is implemented, but output disabled in logback.xml
- Filtering by wildcard is not supported, as this was not a requirement. Exact matches will be required, and the user is reminded that Customer names have spaces, therefore it may be appropriate to surround filter specifications with quote marks.
- No data validation is required. As such, the application will not complain if the user attempts to filter products by price, and provides a negative price (despite this being a logically incorrect input).
- Postcodes in the requirement lack specification for locale, and have been randomly generated using the US Postal System.
- Customers have a First Name and Surname, but requirements only classified "name", so a name field has been created following First + Surname.
- No JavaDoc, hopefully the code can be read plainly. Where appropriate, there are some comments to add explanation.

A small addition has been made to the requirements:
- Entering a price range backwards will result in the application correcting the search term and still obeying your request.
- It is also possible to filter products by exact price, rather than solely by a range. 

Thanks,

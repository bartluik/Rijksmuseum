# Rijksmuseum

The AH Rijksmuseum tech assignment

* Use the Rijksmuseum API, see documentation
  here: https://data.rijksmuseum.nl/object-metadata/api/ (API key to use: XXXX)
* We would like to see an app with at least two screens:
    * An overview page with a list of items:
        * Should be visually split in sections with headers, grouped by author, with the author's
          name in the header.
        * Items should have text and image.
        * Screenshouldbepaginated.
    * A detail page, with more details about the item.
* Loading of data and images should be asynchronous, a loading indicator should be shown.
* Automated tests should be present (full coverage not needed of course).
* Please use Fragments (used heavily in our code base) with a single Activity, or Compose.
* Please use Kotlin (100% of our codebase is).

# Important

This project required the API key to be set, you can add it like `RIJKS_DATA_API_KEY=xxxxx`
to `local.properties`

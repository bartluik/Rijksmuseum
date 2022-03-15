# Rijksmuseum

The Rijksmuseum tech assignment

* Use the Rijksmuseum API, see documentation
  here: https://data.rijksmuseum.nl/object-metadata/api/ (API key to use: XXXX)
* We would like to see an app with at least two screens:
    * An overview page with a list of items:
        * Should be visually split in sections with headers, grouped by author, with the author's
          name in the header.
        * Items should have text and image.
        * Screen should be paginated.
    * A detail page, with more details about the item.
* Loading of data and images should be asynchronous, a loading indicator should be shown.
* Automated tests should be present (full coverage not needed of course).
* Please use Fragments (used heavily in our code base) with a single Activity, or Compose.
* Please use Kotlin (100% of our codebase is).

# The Assessment

After the review, the reviewers get together and attempt to understand if you could be a good fit
for the team. If so, a fun conversation awaits! You’ll be invited to a follow-up interview to
discuss the code from your assessment, and all the interesting things related to good programming.
If your approach is not to our liking, you’ll still be given feedback about what we did and did not
appreciate about the code: this is to make sure you also get something in return for taking the time
to do our test. You’re free to use whichever library you’re used to in your own projects, without
limitations. Just give us a heads-up if you reach for something more exotic that heavily impacts
what the implementation looks like (e.g. Epoxy or similar).

# Important

This project requires the API key to be set, you can add it like `RIJKS_DATA_API_KEY=xxxxx`
to `local.properties`

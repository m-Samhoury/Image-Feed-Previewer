# Image feed demo application

#### This sample was created to showcase my skills and the latest learnings in the Android Framework.

##### To be able to compile this project, you need to add the following in your global gradle properties file:

##### API_ACCESS_KEY

To be able to swap Unsplash images data source with any other data source just implement
the `ImageFeedAdapter<T>` and start using it in your corresponding `Viewmodel`


Used Glide for a more efficient image loading

Used Navigation component to provide the navigation in the app(1 screen for now)

Kotlin-coroutines were used for blocking operations(fetching images list from the server)

Used LiveData and ViewModels as well

Koin was used to glue the different components together in the application

Unit tests are not included, but you can take a look on this repository to take an idea how I
usually write unit tests [over here] (https://github.com/m-Samhoury/Marley-Spoon-Challenge/blob/master/app/src/test/java/com/challenge/marleyspoon/RecipesListScreenTest.kt)





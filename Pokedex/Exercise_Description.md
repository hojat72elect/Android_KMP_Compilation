# American Airlines - Sr. Android Developer Interview

Welcome! This coding interview is designed for Senior Android Developers to be able to demonstrate their skills and proficiencies working with an existing Android project.

---

## Project: Pokédex

Pokémon Enterprises is nearing the initial release of their Pokédex app, but they're in a bit of a bind - there are several issues, technical debt items, and missing features to take care of before they release the app. They have called you in to help push the project across the finish line with your expertise. Complete as many tasks as you're able to before the release!

## Background Info

### Guidelines

1. You have an hour to complete as many tasks as possible.
2. You may implement a solution however you wish as long as they satisfy the acceptance criteria of the task.
   > Acceptance Criteria appear in this format.
3. A variety of libraries/sdks have been added to the project, but you may add any additional libraries you wish to help aid with your solutions.
4. Some code has been provided for you, such as API endpoints and model classes to represent the JSON structure returned by those endpoints.
5. You may use the internet to look up docs and/or guides as you would during normal development.

### API

The app is built upon a REST service called [PokéAPI](https://pokeapi.co/docs/v2). The app only utilizes the `/pokemon` endpoints - documentation for those can be seen [here](https://pokeapi.co/docs/v2#pokemon).

---

## Tasks

### Bugs

- **Fix pokemon list not loading on the main screen:**
  - For some reason the Pokémon list is not loading on the main screen. Please look into it and correct the issue!
    - > Once fixed, Main screen should display a list of Pokémon names.

### Features

- **Implement a detail screen:**
  - We haven't had time to implement the Pokémon detail screen. Please implement one for us - the app should open the detail screen when a list item is tapped on the main screen.
  - Here's the list of acceptance criteria we have:
  - > Written in Kotlin
  - > Adheres to an MVVM architecture
  - > Uses Dependency Injection to supply dependencies
  - > Makes an API call to fetch the details about the selected Pokémon. Endpoint: `/pokemon/{id or name}`
  - > Displays the following items in a well-designed layout (traditional or Compose):
    - > An image of the Pokémon (preferably the Dream World image, JSON key: `sprites.other.dream_world.front_default`)
    - > Name (capitalized)
    - > Height (with appropriate units)
    - > Weight (with appropriate units)
    - > Type(s) (preferably with the appropriate background color as defined in `Type.kt`)
    - > Stats - hp, attack, defense, etc. (name and value)

### Technical Debt

- **Implement a caching layer for the HTTP calls:**
  - In our rush to get the API calls working, we skipped over implementing a cache. Please add a cache so our network calls are more performant!
    - > Solution should cache the response as specified by the `cache-control` headers returned from the API.
    - > When a network resource is requested, solution should use cached value if it is still valid.
- **Finish Dependency Injection within the project**
  - Hilt has been integrated into the project and is working, however it needs to follow best practices. Our `Repository` class has an instance of `Retrofit` injected, and is then instantiating an instance of the API. We should just be injecting the API instance instead.
  - Acceptance criteria:
    - > An instance of `PokemonApi` is provided by a Hilt module, and is injected into `PokemonRepository`.

## Bonus!

- **Add total Pokémon count to the top of the main screen:**
  - > Modify the existing Compose layout to include the total count of Pokémon somewhere on the main screen.
- **Write unit/UI test(s) to cover the project:**
  - > Add JUnit and/or Espresso tests to validate behavior and business logic.

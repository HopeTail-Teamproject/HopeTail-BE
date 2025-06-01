package hello.hello_spring.domain;

public enum AdoptionQuestionType {
    WHY_ADOPT("What is the reason you want to adopt a dog?"),
    FAMILY_MEMBERS("Do you have other family members (e.g., other pets, children) living with you?"),
    LIVING_PLACE("Where will the dog live? (House, apartment, house with a yard, etc.)"),
    OUTSIDE_FREQUENCY("How often will the dog be able to go outside? (Walking time and frequency)"),
    BATH_PLAN("How do you plan to bathe the dog?"),
    EXERCISE("How much exercise do you think the dog will need?"),
    MEAL_MANAGEMENT("How do you plan to manage the dog’s meals? (Food, treats, feeding times)"),
    FINANCIAL("Do you have the financial resources to care for the dog? (e.g., healthcare costs, food, toys, training, etc.)"),
    BEHAVIOR_ISSUE("If any behavioral or living issues arise after adoption, how do you plan to address them?"),
    VACATION_CARE("How will you take care of the dog during vacations or business trips?"),
    TEN_YEAR_RESPONSIBILITY("Are you prepared to take responsibility for the dog for over 10 years after adoption?"),
    OTHER_ANIMALS("Are there any other animals at your home?"),
    OTHER_ANIMAL_DETAIL("If yes, please state their species, age and gender"),
    NEUTERED("If yes, are they neutered?"),
    VACCINATED("If yes, have they been vaccinated in the last 12 month?"),
    EXPERIENCE("Please describe your experience of any previous pet ownership and tell us about the type of home you plan to offer your new pet.");

    private final String question;

    AdoptionQuestionType(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }
}

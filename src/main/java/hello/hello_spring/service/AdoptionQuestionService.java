package hello.hello_spring.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptionQuestionService {

    public List<String> getQuestions() {
        return List.of(
                "What is the reason you want to adopt a dog?",
                "Do you have other family members (e.g., other pets, children) living with you?",
                "Where will the dog live? (House, apartment, house with a yard, etc.)",
                "How often will the dog be able to go outside? (Walking time and frequency)",
                "How do you plan to bathe the dog?",
                "How much exercise do you think the dog will need?",
                "How do you plan to manage the dog’s meals? (Food, treats, feeding times)",
                "Do you have the financial resources to care for the dog? (e.g., healthcare costs, food, toys, training, etc.)",
                "If any behavioral or living issues arise after adoption, how do you plan to address them?",
                "How will you take care of the dog during vacations or business trips?",
                "Are you prepared to take responsibility for the dog for over 10 years after adoption?",
                "Are there any other animals at your home?",
                "If yes, please state their species, age and gender",
                "If yes, are they neutered?",
                "If yes, have they been vaccinated in the last 12 months?",
                "Please describe your experience of any previous pet ownership and tell us about the type of home you plan to offer your new pet."
        );
    }
}

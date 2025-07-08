//package com.carolinawings.webapp.config;
//
//import com.carolinawings.webapp.factory.MenuItemFactory;
//import com.carolinawings.webapp.model.MenuItem;
////import com.carolinawings.webapp.factory.MenuItemFactory;
//import com.carolinawings.webapp.repository.MenuItemRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//@Configuration
//public class MenuDataSeeder {
//
//    @Bean
//    CommandLineRunner seedMenu(MenuItemRepository repo) {
//        return args -> {
//            if(repo.count() == 0) {
//                List<MenuItem> items = List.of(
//                        MenuItemFactory.createMenuItem("Buffalo Shrimp", "Our famous hand-battered shrimp tossed in our award-winning buffalo sauce. Served with bleu cheese or ranch dressing.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Shareables"),
//                        MenuItemFactory.createMenuItem("Catfish Fingers", "Fresh catfish filets sliced, hand-battered & fried. Served with a side of CW's Remoulade Sauce.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Shareables"),
//                        MenuItemFactory.createMenuItem("Texas Tumbleweeds", "Hand-rolled & fried tortillas stuffed with seasoned chicken, black beans, cheese & corn.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Shareables"),
//                        MenuItemFactory.createMenuItem("Quesadillas - Southwest Chicken", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Shareables"),
//                        MenuItemFactory.createMenuItem("Quesadillas - Brisket Tossed in Pig Sauce", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Shareables"),
//                        MenuItemFactory.createMenuItem("Quesadillas - Buffalo Chicken", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Shareables"),
//                        MenuItemFactory.createMenuItem("Chips & Salsa", "Medium spice salsa and freshly fried tortilla chips.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(4.99), "Shareables"),
//                        MenuItemFactory.createMenuItem("Ultimate Nachos - Rob's Chili", "Tortilla chips covered in Rob's Chili, shredded cheese, tomatoes & jalapeños. Served with sour cream & salsa.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Shareables"),
//                        MenuItemFactory.createMenuItem("Ultimate Nachos - Buffalo Chicken", "Tortilla chips covered in fried chicken with your choice of CW wing sauce, shredded cheese, tomatoes & jalapeños. Served with sour cream & salsa.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Shareables"),
//                        MenuItemFactory.createMenuItem("Ultimate Nachos - Pulled Pork", "Tortilla chips covered in pulled-pork BBQ with your choice of sauce, shredded cheese, tomatoes & jalapeños. Served with sour cream & salsa.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Shareables"),
//                        MenuItemFactory.createMenuItem("Mozzarella Sticks", "Crispy, golden brown mozzarella sticks served with a side of marinara", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(7.99), "Shareables"),
//                        MenuItemFactory.createMenuItem("Fried Pickles", "Hand-battered and fried dill pickle chips.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(7.99), "Shareables"),
//                        MenuItemFactory.createMenuItem("Fried Mushrooms", "Hand-battered and fried mushrooms", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(7.99), "Shareables"),
//                        MenuItemFactory.createMenuItem("Rib Bites", "4–5 Bones of our award-winning baby back ribs.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(8.99), "Shareables"),
//                        MenuItemFactory.createMenuItem("Loaded Fries/Tots - Cheese & Bacon", "French fries or wing chips covered in cheese & bacon.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(8.99), "Shareables"),
//                        MenuItemFactory.createMenuItem("Loaded Fries/Tots - Garlic Parmesan", "French fries or wing chips covered in garlic parmesan.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(7.99), "Shareables"),
//                        MenuItemFactory.createMenuItem("Loaded Fries/Tots - Cheese and Chili", "French fries or wing chips covered in cheese and chili.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(8.99), "Shareables"),
//                        MenuItemFactory.createMenuItem("Carolina Sampler", "Fried mushrooms, mozzarella sticks, hand-battered tenders & fried pickles on wing chips. No substitutions.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(13.99), "Shareables"),
//                        MenuItemFactory.createMenuItem("Soup of the Day (Bowl)", "Ask your server for today's special.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(5.99), "Soup and Salads"),
//                        MenuItemFactory.createMenuItem("Rob's Chili", "A hearty meat & tomato based chili with beans & just the right amount of heat!", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(6.99), "Soup and Salads"),
//                        MenuItemFactory.createMenuItem("The Protein Salad", "Turkey, ham and bacon over fresh-cut greens with shredded cheese, carrots, cucumbers, tomatoes, onions, and red cabbage then topped with fresh Grilled Chicken.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(12.99), "Soup and Salads"),
//                        MenuItemFactory.createMenuItem("The Carolina Salad", "Fresh-cut greens with shredded cheese, carrots, red cabbage, cucumbers, tomatoes, onion & croutons.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(8.99), "Soup and Salads"),
//                        MenuItemFactory.createMenuItem("Grilled or Blackened Chicken (Salad Add-on)", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(3.00), "Soup and Salads"),
//                        MenuItemFactory.createMenuItem("Fried Chicken (Salad Add-on)", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(3.00), "Soup and Salads"),
//                        MenuItemFactory.createMenuItem("Buffalo Chicken (Salad Add-on)", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(3.00), "Soup and Salads"),
//                        MenuItemFactory.createMenuItem("CW Chef Salad", "Turkey, ham & bacon over fresh-cut greens with shredded cheese, carrots, red cabbage, cucumbers, tomatoes, onions & croutons.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Soup and Salads"),
//                        MenuItemFactory.createMenuItem("Certified Jumbo Wings - 8 Wings", "Our award-winning chicken wings are deep-fried and pan sautéed in your favorite wing flavor.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Wings and Tenders"),
//                        MenuItemFactory.createMenuItem("Certified Jumbo Wings - 12 Wings", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(14.99), "Wings and Tenders"),
//                        MenuItemFactory.createMenuItem("Certified Jumbo Wings - 18 Wings (up to 2 flavors)", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(22.99), "Wings and Tenders"),
//                        MenuItemFactory.createMenuItem("Certified Jumbo Wings - 24 Wings (up to 3 flavors)", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(27.99), "Wings and Tenders"),
//                        MenuItemFactory.createMenuItem("Certified Jumbo Wings - 50 Wings", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(54.99), "Wings and Tenders"),
//                        MenuItemFactory.createMenuItem("Boneless Wings - 8 Wings", "Hand-battered boneless chicken bites fried to perfection & sautéed in your favorite wing sauce.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Wings and Tenders"),
//                        MenuItemFactory.createMenuItem("Boneless Wings - 12 Wings", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(12.99), "Wings and Tenders"),
//                        MenuItemFactory.createMenuItem("Boneless Wings - 18 Wings (up to 2 flavors)", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(16.99), "Wings and Tenders"),
//                        MenuItemFactory.createMenuItem("Boneless Wings - 24 Wings (up to 3 flavors)", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(22.99), "Wings and Tenders"),
//                        MenuItemFactory.createMenuItem("Boneless Wings - 50 Wings", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(44.99), "Wings and Tenders"),
//                        MenuItemFactory.createMenuItem("Chicken Tenders - 5 Tenders", "Fresh chicken tenders battered & fried to a golden brown. Plain or tossed in one of your favorite wing sauces.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(8.99), "Wings and Tenders"),
//                        MenuItemFactory.createMenuItem("Chicken Tenders - 7 Tenders", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Wings and Tenders"),
//                        MenuItemFactory.createMenuItem("Chicken Tenders - Tender Plate", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(14.99), "Wings and Tenders"),
//                        MenuItemFactory.createMenuItem("Wing & Tenders Plate", "6 wings & 3 chicken tenders with a side of wing chips.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(14.99), "Wings and Tenders"),
//                        MenuItemFactory.createMenuItem("Wing Plate", "Your choice of 8 jumbo wings, 10 boneless wings, or 6 tenders with a side of wing chips.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(14.99), "Wings and Tenders"),
//                        MenuItemFactory.createMenuItem("Baby Back Ribs - Small Rack", "Slow cooked for 12 hours & coated with your choice of BBQ sauce or our Carolina Rub.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(15.99), "Ribs, BBQ, and Seafood"),
//                        MenuItemFactory.createMenuItem("Baby Back Ribs - Large Rack", "A full rack of slow cooked baby back ribs & coated with your choice of BBQ sauce or our Carolina Rub.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(19.99), "Ribs, BBQ, and Seafood"),
//                        MenuItemFactory.createMenuItem("Pulled Pork BBQ Feast", "Pulled pork BBQ piled high & slow-cooked in your choice of BBQ sauce.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(13.99), "Ribs, BBQ, and Seafood"),
//                        MenuItemFactory.createMenuItem("Fried Shrimp Plate", "1/2 lb. of our famous hand-battered shrimp fried to a crispy golden brown. Sauce things up by adding your favorite CW flavor for $0.59", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(13.99), "Ribs, BBQ, and Seafood"),
//                        MenuItemFactory.createMenuItem("CW Flavor Add-on (Shrimp)", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(0.59), "Ribs, BBQ, and Seafood"),
//                        MenuItemFactory.createMenuItem("Fried Catfish Plate", "Fresh catfish filets, hand-battered & fried to a crispy golden brown.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(13.99), "Ribs, BBQ, and Seafood"),
//                        MenuItemFactory.createMenuItem("Combo - Ribs & Wings", "Served with wing chips. Upgrade to a large rack for just $6.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(18.99), "Ribs, BBQ, and Seafood"),
//                        MenuItemFactory.createMenuItem("Combo - Ribs & BBQ", "Served with wing chips. Upgrade to a large rack for just $6.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(19.99), "Ribs, BBQ, and Seafood"),
//                        MenuItemFactory.createMenuItem("Ribhouse Sampler", "A quarter rack of baby back ribs, 6 jumbo wings, & 1/4 lb. of slow-cooked pulled pork BBQ served with a side of wing chips.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(21.99), "Ribs, BBQ, and Seafood"),
//                        MenuItemFactory.createMenuItem("Smokehouse Burger", "CW's classic BBQ sauce, American & Monterey Jack cheese, bacon & topped with onion rings.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(12.99), "Burgers"),
//                        MenuItemFactory.createMenuItem("Carolina Pig Sauce Burger", "House-pattied burger, slow-roasted BBQ, bacon, onion rings, Monterey Jack and American cheese & smothered in our award-winning Carolina Pig Sauce.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(13.59), "Burgers"),
//                        MenuItemFactory.createMenuItem("Black and Bleu Burger", "Cajun blackened seasoning seared in & topped w/ bleu cheese crumbles & LTO.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(11.99), "Burgers"),
//                        MenuItemFactory.createMenuItem("Buffalo Bleu Burger", "Grilled in CW's medium buffalo sauce and topped with bleu cheese crumbles & LTO.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(11.59), "Burgers"),
//                        MenuItemFactory.createMenuItem("Mushroom Jack Burger", "Grilled mushrooms, Monterey Jack cheese & LTO.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(11.99), "Burgers"),
//                        MenuItemFactory.createMenuItem("The Carolina Burger", "Freshly made and seasoned with our signature rub with lettuce, tomato & onion (LTO).", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Burgers"),
//                        MenuItemFactory.createMenuItem("Monterey Jack or American Cheese", "Our seasoned burger topped with the choice of Monterey Jack or American cheese", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.59), "Burgers"),
//                        MenuItemFactory.createMenuItem("Cheese & Bacon", "Topped with American cheese and bacon", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(11.99), "Burgers"),
//                        MenuItemFactory.createMenuItem("Bacon Egg & Cheese Burger", "Topped with bacon, egg, Monterey Jack cheese & LTO.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(12.99), "Burgers"),
//                        MenuItemFactory.createMenuItem("Atomic Burger", "Our seasoned burger topped with Monterey Jack cheese, jalapeños, our signature hot sauce & LTO.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(11.99), "Burgers"),
//                        MenuItemFactory.createMenuItem("Mini Sliders", "(3) Mini Hamburger Sliders, w/ or w/o Cheese, and Pickles", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Burgers"),
//                        MenuItemFactory.createMenuItem("CW's Famous BBQ Sandwich", "Cooked to perfection and piled high. Don't forget to add one of CW's BBQ sauces! Served with a side of coleslaw.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Sandwiches and Wraps"),
//                        MenuItemFactory.createMenuItem("Carolina Chicken", "Fried chicken breast sautéed in your favorite CW's wing sauce. Topped with Monterey Jack cheese, lettuce & tomato.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Sandwiches and Wraps"),
//                        MenuItemFactory.createMenuItem("Carolina Dip", "Chopped sirloin, melted Monterey Jack cheese & Au Jus. Make it Philly style by adding peppers & onions for 59¢.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Sandwiches and Wraps"),
//                        MenuItemFactory.createMenuItem("Philly Style Add-on", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(0.59), "Sandwiches and Wraps"),
//                        MenuItemFactory.createMenuItem("Carolina Club", "Served on toasted wheat berry bread with fresh cut lettuce and tomato, stacked with turkey, ham, and bacon.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Sandwiches and Wraps"),
//                        MenuItemFactory.createMenuItem("Kickin' Chicken Phily", "Diced grilled chicken, onion & peppers smothered in Monterey Jack cheese on a hoagie roll.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Sandwiches and Wraps"),
//                        MenuItemFactory.createMenuItem("Brisket Cheese Melt", "Slow cooked Brisket, melted American cheese served on Country White Bread. Served with Au-Jus.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Sandwiches and Wraps"),
//                        MenuItemFactory.createMenuItem("Myra's Chicken Wrap", "Grilled chicken, Monterey Jack cheese & our Doc's Special sauce. That's It!", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.59), "Sandwiches and Wraps"),
//                        MenuItemFactory.createMenuItem("Grilled Chicken Wrap", "Diced grilled chicken tossed with fresh cut lettuce, diced tomato, & ranch dressing.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.59), "Sandwiches and Wraps"),
//                        MenuItemFactory.createMenuItem("Philly Wrap", "Grilled sirloin, Monterey Jack, onions & peppers. Served with a side of Au Jus.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Sandwiches and Wraps"),
//                        MenuItemFactory.createMenuItem("Buffalo Wrap", "Your choice of our boneless chicken bites or golden fried shrimp tossed in medium wing sauce with fresh cut lettuce & tomatoes.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Sandwiches and Wraps"),
//                        MenuItemFactory.createMenuItem("BLT Wrap", "Crisp bacon, fresh cut lettuce, tomato & mayo.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(8.99), "Sandwiches and Wraps"),
//                        MenuItemFactory.createMenuItem("Carolina Club Wrap", "Our Famous Carolina Club served in a wrap!", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Sandwiches and Wraps"),
//                        MenuItemFactory.createMenuItem("Veggie Wrap", "Our grilled veggies with parmesan cheese in a tomato basil wrap. Served with marinara dip on the side.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Sandwiches and Wraps"),
//                        MenuItemFactory.createMenuItem("Wing Chips", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(3.59), "Side Items"),
//                        MenuItemFactory.createMenuItem("Wing Chips - Cheese Add-on", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(1.99), "Side Items"),
//                        MenuItemFactory.createMenuItem("French Fries", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(3.59), "Side Items"),
//                        MenuItemFactory.createMenuItem("French Fries - Cheese Add-on", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(1.99), "Side Items"),
//                        MenuItemFactory.createMenuItem("Grilled Veggies", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(3.59), "Side Items"),
//                        MenuItemFactory.createMenuItem("Coleslaw", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(4.59), "Side Items"),
//                        MenuItemFactory.createMenuItem("Side Salad", "Your choice of our Carolina or Caesar salad.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(4.59), "Side Items"),
//                        MenuItemFactory.createMenuItem("Substitution - Switch Any Side Item", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(0.59), "Side Items"),
//                        MenuItemFactory.createMenuItem("Substitution - Upgrade to Onion Rings", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(1.99), "Side Items"),
//                        MenuItemFactory.createMenuItem("Substitution - Upgrade To Side Salad", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(1.29), "Side Items"),
//                        MenuItemFactory.createMenuItem("Substitution - Add A Side Salad", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(1.99), "Side Items"),
//                        MenuItemFactory.createMenuItem("Tater Tots", "Crispy, golden brown tater tots", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(4.59), "Side Items,")
//                        );
//                repo.saveAll(items);
//                System.out.println("✅ Menu items seeded.");
//
//            }
//            else {
//                System.out.println("Menu items already seeded.");
//            }
//        };
//    }
//}

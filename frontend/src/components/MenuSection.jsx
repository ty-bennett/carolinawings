import { FaPepperHot } from "react-icons/fa";
import MenuItem from "../components/MenuItem"
import Footer from "../components/Footer";
import Map from "../components/Map";
import section from "../components/MenuSection.module.css";

const MenuSection = () => {
    return(
    <>
      <div className={section.contentcontainer}>
        <img className={section.quesadilla} src="/foodimages/quesadilla.jpg"/>
        <h1 className={section.sectiontitle}>SHAREABLES</h1>
        <div className={section.menuitemcontainer}>
          <MenuItem 
            isSpicy={true}
            name="Buffalo Shrimp"
            description="Our famous hand-battered shrimp tossed in our award-winning buffalo sauce. Served with bleu cheese or ranch dressing." 
            price="$9.99"
          />
         
          <MenuItem 
            name="Catfish Fingers"
            description="Fresh catfish filets sliced, hand-battered & fried. Served with a side of CW's Remoulade Sauce."
            price="$10.99"
          />

          <MenuItem 
            name="Texas Tumbleweeds"
            description="Hand-rolled & fried tortillas stuffed with seasoned chicken, black beans, cheese & corn. Served with a side of our homemade southwest sauce."
            price="$9.99"
          />

          <MenuItem 
            isFavorite={true}
            name="Quesadillas"
            description={
              <>
                <ul className={section.quesadillaul}>
                  <li>Cheese................................................$8.99</li>
                  <li>Chicken.............................................$10.99</li>
                  <li>Southwest Chicken..........................$10.99</li>
                  <li>Brisket tossed in Pig Sauce............$10.99</li>
                  <li><FaPepperHot />&nbsp;Buffalo Chicken...........................$10.99</li>
                </ul>
              </>
            }
            price="$10.99"
          />

          <MenuItem 
            name="Chips & Salsa"
            description="Medium spice salsa and freshly fried tortilla chips."
            price="$4.99"
          />

          <MenuItem 
            name="Ultimate Nachos"
            description={
              <>
                <p>Tortilla chips covered in shredded cheese, tomatoes & jalapeños. Served with a side of sour cream & salsa.</p>
                  <ul className={section.quesadillaul}>
                  <li>Rob's Chili..................$9.99</li>
                  <li><FaPepperHot />Buffalo Chicken..................$10.99</li>
                  <li>Pulled Pork........................$10.99</li>
                </ul>
              </>
            }
          />

          <MenuItem 
            name="Mozzarella Sticks"
            description="Classic fried mozzarella sticks."
            price="$7.99"
          />

          <MenuItem 
            name="Fried Pickles"
            description="Hand-battered and fried dill pickle chips."
            price="$7.99"
          />

          <MenuItem 
            isVegetarian={true}
            name="Fried Mushrooms"
            description="Fried mushrooms served with dipping sauce."
            price="$7.99"
          />

          <MenuItem 
            name="Rib Bites"
            description="4-5 Bones of our award-winning baby back ribs."
            price="$8.99"
          />

          <MenuItem 
            isFavorite={true}
            name="Loaded Wing Chips/Fries/Tots"
            description={
              <>
                <p>Your choice of French fries, wing chips, or tater tots topped with:</p>
                <ul>
                  <li>Cheese & Bacon..................$8.99</li>
                  <li>Garlic Parmesan...................$7.99</li>
                  <li>Cheese and Chili..................$8.99</li>
                </ul>
              </>
            }
            price="$8.99"
          />

          <MenuItem 
            name="Carolina Sampler"
            description="Fried mushrooms, mozzarella sticks, hand-battered tenders & fried pickles all on a bed of our legendary wing chips. No substitutions please."
            price="$13.99"
          />

        </div>
          <img className={section.quesadilla} src="/foodimages/grilledchixsalad.jpeg"/>
          <h1 className={section.sectiontitle}>SOUPS & SALADS</h1>
          <div className={section.menuitemcontainer}>

          <MenuItem
            name="Soup of the Day (Bowl)"
            description="Ask your server for today's special."
            price="$5.99"
          />

          <MenuItem
            name="Rob's Chili"
            description="A hearty meat & tomato based chili with beans & just the right amount of heat!"
            price="$6.99"
            isSpicy
          />

          <MenuItem
            name="The Protein Salad"
            description="Turkey, ham and bacon over fresh-cut greens with shredded cheese, carrots, cucumbers, tomatoes, onions, and red cabbage then topped with fresh Grilled Chicken."
            price="$12.99"
            isFavorite
          />

          <MenuItem
            name="The Carolina Salad"
            description="Fresh-cut greens with shredded cheese, carrots, red cabbage, cucumbers, tomatoes, onion & croutons."
            price="$8.99"
            isVegetarian
          />

          <MenuItem
            name="Grilled or Blackened Chicken (Add-On)"
            description="Optional add-on for salads."
            price="$3.00"
          />

          <MenuItem
            name="Fried Chicken (Add-On)"
            description="Optional add-on for salads."
            price="$3.00"
          />

          <MenuItem
            name="Buffalo Chicken (Add-On)"
            description="Optional add-on for salads with a spicy kick."
            price="$3.00"
            isSpicy
          />

          <MenuItem
            name="CW Chef Salad"
            description="Turkey, ham & bacon over fresh-cut greens with shredded cheese, carrots, red cabbage, cucumbers, tomatoes, onions & croutons."
            price="$10.99"
          />
        </div>
          <img className={section.quesadilla} src="/foodimages/wingsandtenders.jpeg"/>
          <h1 className={section.sectiontitle}>WINGS & TENDERS</h1>
          <div className={section.menuitemcontainer}>
            <MenuItem
              name="8 Jumbo Wings"
              description="Certified jumbo wings deep-fried and pan sautéed in your favorite wing flavor."
              price="$10.99"
            />

            <MenuItem
              name="12 Jumbo Wings"
              description="Certified jumbo wings deep-fried and pan sautéed in your favorite wing flavor."
              price="$14.99"
            />

            <MenuItem
              name="18 Jumbo Wings"
              description="Certified jumbo wings deep-fried and pan sautéed in your favorite wing flavor. (Up to 2 flavors)"
              price="$22.99"
            />

            <MenuItem
              name="24 Jumbo Wings"
              description="Certified jumbo wings deep-fried and pan sautéed in your favorite wing flavor. (Up to 3 flavors)"
              price="$27.99"
            />

          <MenuItem
            name="50 Jumbo Wings"
            description="Certified jumbo wings deep-fried and pan sautéed in your favorite wing flavor."
            price="$54.99"
          />

          <MenuItem
            name="8 Boneless Wings"
            description="Hand-battered boneless chicken bites fried to perfection & sautéed in your favorite wing sauce."
            price="$9.99"
          />
      
          <MenuItem
            name="12 Boneless Wings"
            description="Hand-battered boneless chicken bites fried to perfection & sautéed in your favorite wing sauce."
            price="$12.99"
          />
      
          <MenuItem
            name="18 Boneless Wings"
            description="Hand-battered boneless chicken bites fried to perfection & sautéed in your favorite wing sauce. (Up to 2 flavors)"
            price="$16.99"
          />
      
          <MenuItem
            name="24 Boneless Wings"
            description="Hand-battered boneless chicken bites fried to perfection & sautéed in your favorite wing sauce. (Up to 3 flavors)"
            price="$22.99"
          />
      
          <MenuItem
            name="50 Boneless Wings"
            description="Hand-battered boneless chicken bites fried to perfection & sautéed in your favorite wing sauce."
            price="$44.99"
          />

          <MenuItem
            name="5 Chicken Tenders"
            description="Fresh chicken tenders battered & fried to a golden brown. Plain or tossed in one of your favorite wing sauces."
            price="$8.99"
          />
      
          <MenuItem
            name="7 Chicken Tenders"
            description="Fresh chicken tenders battered & fried to a golden brown. Plain or tossed in one of your favorite wing sauces."
            price="$10.99"
          />
      
          <MenuItem
            name="Tender Plate"
            description="Fresh chicken tenders battered & fried to a golden brown. Plain or tossed in one of your favorite wing sauces."
            price="$14.99"
          />

          <MenuItem
            name="Wing & Tenders Plate"
            description="6 wings & 3 chicken tenders with a side of wing chips."
            price="$14.99"
          />
      
          <MenuItem
            name="Wing Plate"
            description="Your choice of 8 jumbo wings, 10 boneless wings, or 6 tenders with a side of wing chips."
            price="$14.99"
          />
          </div>
          <img className={section.quesadilla} src="/foodimages/ribs.jpeg"/>
          <h1 className={section.sectiontitle}>RIBS, BBQ, SEAFOOD</h1>
          <div className={section.menuitemcontainer}>
          <MenuItem
            name="Small Rack of Baby Back Ribs"
            description="Slow cooked for 12 hours & coated with your choice of BBQ sauce or our Carolina Rub."
            price="$15.99"
          />
          
          <MenuItem
            name="Large Rack of Baby Back Ribs"
            description="Slow cooked for 12 hours & coated with your choice of BBQ sauce or our Carolina Rub."
            price="$19.99"
          />
          
          <MenuItem
            name="Pulled Pork BBQ Feast"
            description="Pulled pork BBQ piled high & slow-cooked in your choice of BBQ sauce."
            price="$13.99"
          />
          
          <MenuItem
            name="Fried Shrimp Plate"
            description="1/2 lb. of our famous hand-battered shrimp fried to a golden brown. Sauce things up by adding your favorite CW flavor for $0.59."
            price="$13.99"
          />
          
          <MenuItem
            name="Your Favorite CW Flavor (Add-On)"
            description="Add your favorite CW flavor to any seafood item."
            price="$0.59"
          />
          
                    <MenuItem
                      name="Fried Catfish Plate"
                      description="Fresh catfish filets, hand-battered & fried."
            price="$13.99"
          />
          
          <MenuItem
            name="Ribs & Wings Combo"
            description="Served with wing chips. Upgrade to a large rack for just $6."
            price="$18.99"
          />
          
          <MenuItem
            name="Ribs & BBQ Combo"
            description="Served with wing chips. Upgrade to a large rack for just $6."
            price="$19.99"
          />
          
          <MenuItem
            name="Ribhouse Sampler"
            description="A quarter rack of baby back ribs, 6 jumbo wings, & 1/4 lb. of slow-cooked pulled pork BBQ served with a side of wing chips."
            price="$21.99"
          />
          
          </div>
          <img className={section.quesadilla} src="/foodimages/burgers.jpeg"/>
          <h1 className={section.sectiontitle}>BURGERS</h1>
          <div className={section.menuitemcontainer}>
         <MenuItem
            name="Smokehouse Burger"
            description="CW's classic BBQ sauce, American & Monterey Jack cheese, bacon & topped with onion rings."
            price="$12.99"
          />
          
          <MenuItem
            name="Carolina Pig Sauce Burger"
            description="House-pattied burger, slow-roasted BBQ, bacon, onion rings, cheese & smothered in our award-winning Carolina Pig Sauce."
            price="$13.59"
          />
          
          <MenuItem
            name="Black and Bleu Burger"
            description="Cajun blackened seasoning seared in & topped w/ bleu cheese crumbles & LTO."
            price="$11.99"
          />
          
          <MenuItem
            name="Buffalo Bleu Burger"
            description="Grilled in CW's medium buffalo sauce and topped with bleu cheese crumbles & LTO."
            price="$11.59"
            isSpicy
          />
          
          <MenuItem
            name="Mushroom Jack Burger"
            description="Grilled mushrooms, Monterey Jack cheese & LTO."
            price="$11.99"
          />
          
          <MenuItem
            name="The Carolina Burger"
            description="With lettuce, tomato & onion (LTO)."
            price="$9.99"
          />
          
          <MenuItem
            name="Monterey Jack or American Cheese Burger"
            description="Topped with your choice of Monterey Jack or American cheese & LTO."
            price="$10.59"
          />
          
          <MenuItem
            name="Cheese & Bacon Burger"
            description="Topped with cheese & bacon."
            price="$11.99"
          />
          
          <MenuItem
            name="Bacon Egg & Cheese Burger"
            description="Topped with bacon, egg, Monterey Jack cheese & LTO."
            price="$12.99"
          />
          
          <MenuItem
            name="Atomic Burger"
            description="Our seasoned burger topped with Monterey Jack cheese, jalapeños and our signature hot sauce & LTO."
            price="$11.99"
            isSpicy
          />
          
          <MenuItem
            name="Mini Sliders"
            description="(3) Mini Sliders with cheese and pickles."
            price="$9.99"
          />
          
          </div>
          <img className={section.quesadilla} src="/foodimages/bbqsand.jpeg"/>
          <h1 className={section.sectiontitle}>SANDWICHES & WRAPS</h1>
          <div className={section.menuitemcontainer}>
          
          <MenuItem
            name="CW's Famous BBQ Sandwich"
            description="Cooked to perfection and piled high. Don't forget to add one of CW's BBQ sauces! Served with a side of coleslaw."
            price="$9.99"
          />
          
          <MenuItem
            name="Ultimate Carolina Chicken"
            description="Fried chicken breast sautéed in your favorite CW's wing sauce. Topped with Monterey Jack cheese, lettuce & tomato."
            price="$10.99"
            isSpicy
          />
          
          <MenuItem
            name="Carolina Dip"
            description="Chopped sirloin, melted Monterey Jack cheese & Au Jus. Make it Philly style by adding peppers & onions for 59¢."
            price="$10.99"
          />
          
          <MenuItem
            name="Philly Style (Add-On)"
            description="Add peppers & onions to make it Philly style."
            price="$0.59"
          />
          
          <MenuItem
            name="Carolina Club"
            description="Served on toasted wheat berry bread."
            price="$10.99"
          />
          
          <MenuItem
            name="Kickin' Chicken Phily"
            description="Diced grilled chicken, onion & peppers smothered in Monterey Jack cheese on a hoagie roll."
            price="$9.99"
          />
          
          <MenuItem
            name="Brisket Cheese Melt"
            description="Slow cooked Brisket, melted American cheese served on Country White Bread. Served with Au-Jus."
            price="$10.99"
          />
          
          <MenuItem
            name="Myra's Chicken Wrap"
            description="Grilled chicken, Monterey Jack cheese & our Doc's Special sauce. That's it!"
            price="$9.59"
          />
          
          <MenuItem
            name="Grilled Chicken Wrap"
            description="Diced grilled chicken tossed with fresh cut lettuce, diced tomato, & ranch dressing."
            price="$9.59"
          />
          
          <MenuItem
            name="Philly Wrap"
            description="Grilled sirloin, Monterey Jack, onions & peppers. Served with a side of Au Jus."
            price="$10.99"
          />
          
          <MenuItem
            name="Buffalo Wrap"
            description="Your choice of our boneless chicken bites or golden fried shrimp tossed in medium wing sauce with fresh cut lettuce & tomatoes."
            price="$9.99"
            isSpicy
          />
          
          <MenuItem
            name="BLT Wrap"
            description="Crisp bacon, fresh cut lettuce, tomato & mayo."
            price="$8.99"
          />
          
          <MenuItem
            name="Carolina Club Wrap"
            description="Our Famous Carolina Club served in a wrap!"
            price="$9.99"
          />
          
          <MenuItem
            name="Veggie Wrap"
            description="Our grilled veggies with parmesan cheese. Served with marinara dip on the side."
            price="$9.99"
            isVegetarian
          />
          </div>
          <img className={section.quesadilla} src="/foodimages/chips.jpeg"/>
          <h1 className={section.sectiontitle}>SIDE ITEMS</h1>
          <div className={section.menuitemcontainer}>
          <MenuItem
            name="Wing Chips"
            description="Crispy house-fried wing chips."
            price="$3.59"
          />
          
          <MenuItem
            name="Cheese (Add-On for Wing Chips)"
            description="Add cheese to your wing chips."
            price="$1.99"
          />
          
          <MenuItem
            name="French Fries"
            description="Golden fried French fries."
            price="$3.59"
          />
          
          <MenuItem
            name="Cheese (Add-On for Fries)"
            description="Add cheese to your French fries."
            price="$1.99"
          />
          
          <MenuItem
            name="Grilled Veggies"
            description="Seasoned grilled vegetable medley."
            price="$3.59"
          />
          
          <MenuItem
            name="Coleslaw"
            description="Classic house-made coleslaw."
            price="$4.59"
          />
          
          <MenuItem
            name="Side Salad"
            description="Your choice of our Carolina or Caesar salad."
            price="$4.59"
          />
          
          <MenuItem
            name="Switch Any Side Item"
            description="Substitute any standard side item."
            price="$0.59"
          />
          
          <MenuItem
            name="Upgrade to Onion Rings"
            description="Upgrade your side to onion rings."
            price="$1.99"
          />
          
          <MenuItem
            name="Upgrade to Side Salad"
            description="Upgrade your side to a Carolina or Caesar salad."
            price="$1.29"
          />
          
          <MenuItem
            name="Add a Side Salad"
            description="Add an extra side salad to any order."
            price="$1.99"
          />
          
          <MenuItem
            name="Tater Tots"
            description="Crispy golden tater tots."
            price="$4.59"
          />
        </div>
        </div>
        <Map /> 
        <Footer />
      </>
    );
  }

export default MenuSection;

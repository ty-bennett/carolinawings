import { FaPepperHot } from "react-icons/fa";
import MenuItem from "../components/MenuItem"
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
            isVegetarian={false}
            isSpicy={false}
            isFavorite={false}
            name="Catfish Fingers"
            description="Fresh catfish filets sliced, hand-battered & fried. Served with a side of CW's Remoulade Sauce."
            price="$10.99"
          />

          <MenuItem 
            isVegetarian={false}
            isSpicy={false}
            isFavorite={false}
            name="Texas Tumbleweeds"
            description="Hand-rolled & fried tortillas stuffed with seasoned chicken, black beans, cheese & corn."
            price="$9.99"
          />

          <MenuItem 
            isVegetarian={false}
            isSpicy={false}
            isFavorite={false}
            name="Quesadillas"
            description={
              <>
                <ul className={section.quesadillaul}>
                  <li>Cheese...................................$8.99</li>
                  <li>Chicken..................................$10.99</li>
                  <li>Southwest Chicken.........................$10.99</li>
                  <li>Brisket tossed in Pig Sauce................$10.99</li>
                  <li><FaPepperHot />Buffalo Chicken.................$10.99</li>
                </ul>
              </>
            }
            price="$10.99"
          />

          <MenuItem 
            isVegetarian={true}
            isSpicy={true}
            isFavorite={false}
            name="Chips & Salsa"
            description="Medium spice salsa and freshly fried tortilla chips."
            price="$4.99"
          />

          <MenuItem 
            isVegetarian={false}
            isSpicy={true}
            isFavorite={true}
            name="Ultimate Nachos"
            description={
              <>
                <p>Tortilla chips covered in shredded cheese, tomatoes & jalapeños. Served with a side of sour cream & salsa.</p>
                <p>Choose your topping:</p>
                <ul>
                  <li>Rob's Chili</li>
                  <li>Buffalo Chicken</li>
                  <li>Pulled Pork</li>
                </ul>
              </>
            }
            price="$10.99"
          />

          <MenuItem 
            isVegetarian={true}
            isSpicy={false}
            isFavorite={false}
            name="Mozzarella Sticks"
            description="Classic fried mozzarella sticks."
            price="$7.99"
          />

          <MenuItem 
            isVegetarian={true}
            isSpicy={false}
            isFavorite={false}
            name="Fried Pickles"
            description="Hand-battered and fried dill pickle chips."
            price="$7.99"
          />

          <MenuItem 
            isVegetarian={true}
            isSpicy={false}
            isFavorite={false}
            name="Fried Mushrooms"
            description="Fried mushrooms served with dipping sauce."
            price="$7.99"
          />

          <MenuItem 
            isVegetarian={false}
            isSpicy={false}
            isFavorite={true}
            name="Rib Bites"
            description="4-5 Bones of our award-winning baby back ribs."
            price="$8.99"
          />

          <MenuItem 
            isVegetarian={false}
            isSpicy={false}
            isFavorite={false}
            name="Loaded Fries/Tots"
            description={
              <>
                <p>Your choice of French fries or wing chips, topped with:</p>
                <ul>
                  <li>Cheese & Bacon – $8.99</li>
                  <li>Garlic Parmesan – $7.99</li>
                  <li>Cheese and Chili – $8.99</li>
                </ul>
              </>
            }
            price="$8.99"
          />

          <MenuItem 
            isVegetarian={false}
            isSpicy={false}
            isFavorite={true}
            name="Carolina Sampler"
            description="Fried mushrooms, mozzarella sticks, hand-battered tenders & fried pickles all on a bed of our legendary wing chips. No substitutions please."
            price="$13.99"
          />

          </div>
      </div>
    </>
  );
}

export default MenuSection;

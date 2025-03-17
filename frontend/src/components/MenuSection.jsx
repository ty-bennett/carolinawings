import MenuItem from "../components/MenuItem"
import section from "../components/MenuSection";

const MenuSection = () => {
  return(
    <>
      <div className={section.contentcontainer}> 
        <h1 className={section.title}>Shareables</h1>
        <div className={section.rowcontainer}>   
          <MenuItem isFavorite={true}
                    isVegetarian={false}
                    name={"BUFFALO SHRIMP"}
                    description={"Our famous hand-battered shrimp tossed in our award-winning buffalo sauce. Served with bleu cheese or ranch dressing."}
                    price={9.99} 
          />
          <MenuItem isFavorite={false}
                    isVegetarian={false}
                    name={"QUESADILLA"}
                    descripton={["Tomato basil tortilla with three cheese served with your choice of", <span>southwest mix, chicken, brisket or cheese</span>]}
                    price={10.99}
          />
        </div>
      </div>
    </>
  );
}

export default MenuSection;
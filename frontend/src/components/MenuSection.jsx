import MenuItem from "../components/MenuItem"

const MenuSection = () => {
  return(
    <MenuItem isFavorite={true}
              isVegetarian={false}
              name={"BUFFALO SHRIMP"}
              description={"Our famous hand-battered shrimp tossed in our award-winning buffalo sauce. Served with bleu cheese or ranch dressing."}
              price={9.99} />
  );
}

export default MenuSection;
import NavBar from "../components/NavBar"
import Sauces from "../components/Sauces"
import menu from "../components/MenuHeader.module.css"
// import MenuSection from "../components/MenuSection"
const Menu = () => {
  return (
    <>
      <main>
        <NavBar />
        <div className={menu.headercontainer}>
          <h1 className={menu.menuheader}>MENU</h1>
        </div>
        <div className={menu.discountcontainer}>
          <h2 className={menu.discountheader}>Military & Emergency Personnel</h2>
          <h3 className={menu.discountpercent}>15% OFF</h3>
          <h4 className={menu.discountp}>All service members, police officers, and emergency personnel are eligible. </h4>
        </div>
        <Sauces />
      </main>
    </>
  );
}

export default Menu;
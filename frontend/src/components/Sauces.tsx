import sauces from './Sauces.module.css'

function Sauces() {
  return(
    <div className={sauces.bgimg}>
      <div className={sauces.headercontainer}>
        <h1 className={sauces.header}>Wing & Rib Flavors</h1>
      </div>
      <div className={sauces.contentcontainer}>
        <div className={sauces.cardcontainer}>
          <div className={sauces.card}>
            <h1 className={sauces.sauceheader}>Sauces</h1>
            <div className={sauces.saucecontainer}>
              <p className={sauces.text}> MILD </p>
              <p className={sauces.text}> MILD HONEY </p>
              <p className={sauces.text}> BBQ</p>
              <p className={sauces.text}> TERIYAKI </p>
              <p className={sauces.text}> TERI-BBQ </p>
              <p className={sauces.text}> DOC'S BBQ </p>
              <p className={sauces.text}> HONEY BBQ </p>
              <p className={sauces.text}> HOT GARLIC </p>
              <p className={sauces.text}> HONEY MUSTARD </p>
              <p className={sauces.text}> GARLIC PARMESAN </p>
              <p className={sauces.text}> MANGO HABANERO </p>
            </div>
          </div>
          <div className={sauces.card}>
            <p className={sauces.text}>🌶text</p>
          </div>
          <div className={sauces.card}>
            <p className={sauces.text}>🌶text</p>
          </div>
          <div className={sauces.card}>
          <h1 className={sauces.sauceheader}>Spice Meter</h1>
            <p className={sauces.text}>🌶text</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Sauces;
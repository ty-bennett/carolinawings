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
              <p className={sauces.text}> DOC'S SPECIAL</p>
            </div>
          </div>
          <div className={sauces.card}>
            <h1 className={sauces.sauceheader}>Sauces</h1>
            <div className={sauces.saucecontainer}>
            <p className={sauces.text}>&#127798; MEDIUM</p>
            <p className={sauces.text}>&#127798; HOT HONEY</p>
            <p className={sauces.text}>&#127798; CAJUN HONEY</p>
            <p className={sauces.text}>&#127798; TERI-HOT</p>
            <p className={sauces.text}>&#127798; BUFFALO CAJUN RANCH</p>
            <p className={sauces.text}>&#127798; HOT HONEY MUSTARD</p>
            <p className={sauces.text}>&#127798;&#127798; HOT</p>
            <p className={sauces.text}>&#127798;&#127798; TERI-CAJUN</p>
            <p className={sauces.text}>&#127798;&#127798; CAJUN</p>
            <p className={sauces.text}>&#127798;&#127798; FIRE ISLAND</p>
            <p className={sauces.text}>&#127798;&#127798;&#127798; BLISTERING</p>
            <p className={sauces.text}>&#127798;&#127798;&#127798;&#127798; BEYOND BLISTERING</p>
            </div>
          </div>
          <div className={sauces.card}>
          <h1 className={sauces.sauceheader}>BBQ Flavors</h1>
            <p className={sauces.text}>CW'S CLASSIC BBQ</p>
            <p className={sauces.text}>HONEY BBQ</p>
            <p className={sauces.text}>CW'S GOLD SAUCE</p>
            <p className={sauces.text}>CAROLINA PIG SAUCE</p>
            <p className={sauces.text}>DOC'S BBQ</p>
            <p className={sauces.text}>CAROLINA RED</p>
          <h1 className={sauces.sauceheader}>DRY RUB</h1>
            <p className={sauces.text}>CAROLINA RUB</p>
            <p className={sauces.text}>LEMON PEPPER</p>
          </div>
          <div className={sauces.card}>
          <h1 className={sauces.sauceheader}>Spice Meter</h1>
            <p className={sauces.text}>&#127798; A LITTLE BIT-O-HEAT</p>
            <p className={sauces.text}>&#127798;&#127798; OKAY... I CAN FEEL THAT</p>
            <p className={sauces.text}>&#127798;&#127798;&#127798; I'VE LOST MY TASTE BUDS</p>
            <p className={sauces.text}>&#127798;&#127798;&#127798;&#127798; I CAN'T FEEL MY FACE</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Sauces;
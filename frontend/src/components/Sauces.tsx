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
            <p className={sauces.text}>Balls</p>
          </div>
          <div className={sauces.card}>
            <p className={sauces.text}>Balls</p>
          </div>
          <div className={sauces.card}>
            <p className={sauces.text}>Balls</p>
          </div>
          <div className={sauces.card}>
            <p className={sauces.text}>Balls</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Sauces;
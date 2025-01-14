//TODO: 
// background image that stays when you scroll
// floating white div that scrolls down to location component
//  specials div that links to specials page
// balls

import goodeats from './GoodEats.module.css';

function GoodEats() {
    return (
        <div className={goodeats.bgimg}>
            <div className={goodeats.floatingdiv}>
                <h1 className={goodeats.goodeatsheading}>Good Eats</h1>
                <h3 className={goodeats.subheader}>Carolina Wings & Rib House</h3>
                <button className={goodeats.chooselocationbutton}>Choose Location</button>
            </div>
        </div>
    )
}

export default GoodEats;
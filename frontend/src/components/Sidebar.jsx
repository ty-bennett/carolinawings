// Sidebar.js
function Sidebar({ onSelect }) {
  return (
    <div className="bg-darkred w-1/6 h-full shadow-md border-sm">
      <h2 className="bg-darkred text-white text-2xl text-center p-4 mt-2">Restaurant Admin Panel</h2>
      <div className="border-t border-gray-400 mx-2 shadow-md" />
      <button
        onClick={() => onSelect("menus")}
        className="bg-darkred text-white text-xl w-full text-left p-3 cursor-pointer hover:bg-yellow transition duration-200"
      >
        Menus
      </button>
      <div className="border-t border-gray-400 mx-2 shadow-md" />
      <button
        onClick={() => onSelect("orders")}
        className="bg-darkred text-white text-xl w-full text-left p-3 cursor-pointer"
      >
        Orders
      </button>
      <div className="border-t border-gray-400 mx-2 shadow-md" />
      <button
        onClick={() => onSelect("hours")}
        className="bg-darkred text-white text-xl w-full text-left p-3 cursor-pointer"
      >
        Hours
      </button>
    </div>
  );
}

export default Sidebar;

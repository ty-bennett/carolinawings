function Sidebar({ onSelect }) {
  return (
    <div className="bg-darkred w-1/6 h-full shadow-md border-sm">
      <h2 className="bg-darkred text-white text-2xl text-center p-4 mt-2">Restaurant Admin Panel</h2>
      <div className="border-t border-gray-400 mx-2 shadow-md" />

      {/* Menus Button */}
      <button
        className={`text-xl w-full text-left p-3 cursor-pointer transition duration-200 ${
          onSelect === "menus" ? "bg-yellow text-black font-semibold" : "bg-darkred text-white hover:bg-yellow"
        }`}
      >
        Menus
      </button>
      <div className="border-t border-gray-400 mx-2 shadow-md" />

      {/* Orders Button */}
      <button
        className={`text-xl w-full text-left p-3 cursor-pointer transition duration-200 ${
          onSelect === "orders" ? "bg-yellow text-black font-semibold" : "bg-darkred text-white hover:bg-yellow"
        }`}
      >
        Orders
      </button>
      <div className="border-t border-gray-400 mx-2 shadow-md" />

      {/* Hours Button */}
      <button
        className={`text-xl w-full text-left p-3 cursor-pointer transition duration-200 ${
          onSelect === "hours" ? "bg-yellow text-black font-semibold" : "bg-darkred text-white hover:bg-yellow"
        }`}
      >
        Hours
      </button>
    </div>
  );
}

export default Sidebar;

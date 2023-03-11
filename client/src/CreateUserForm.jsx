import React, { useReducer } from "react";

// const API_URL = import.meta.env.VITE_API_URL || "http://localhost:5000";

// const API_URL = process.env.VITE_API_URL;
// const API_KEY = process.env.VITE_API_KEY;

const API_URL = import.meta.env.VITE_API_URL;
const API_KEY = import.meta.env.VITE_API_KEY;


console.log("API_URL:", API_URL);
console.log("API_KEY:", API_KEY);

const initialState = {
  firstName: "",
  lastName: "",
  email: "",
  gender: "",
  bio: "",
};

function reducer(state, action) {
  switch (action.type) {
    case "updateField":
      return { ...state, [action.field]: action.value };
    case "resetForm":
      return initialState;
    default:
      throw new Error();
  }
}

export const CreateUserForm = ({ setTriggerRefresh }) => {
  const [state, dispatch] = useReducer(reducer, initialState);

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await fetch(`${API_URL}/user`, {
        method: "POST",
        headers: { 
          "Content-Type": "application/json" ,
          Authorization: `ApiKey ${API_KEY}`
        },
        body: JSON.stringify(state),
      });

      console.log(response.data);
      console.log(state);
      dispatch({ type: "resetForm" });
      setTriggerRefresh((prevValue) => !prevValue);
    } catch (error) {
      console.error(error);
    }
  };

  const handleChange = (event) => {
    dispatch({
      type: "updateField",
      field: event.target.name,
      value: event.target.value,
    });
  };

  return (
    <div className="flex justify-center mt-10">
      <form className="w-1/2 p-4 bg-white rounded-lg shadow-md">
        <h1 className="text-3xl font-bold text-gray-700 text-center">
          Create User
        </h1>
        <div className="mb-2">
          <label
            htmlFor="firstName"
            className="block mb-1 font-bold text-gray-700"
          >
            First Name:
          </label>
          <input
            type="text"
            id="firstName"
            name="firstName"
            value={state.firstName}
            onChange={handleChange}
            className="w-full px-2 py-1 leading-tight text-gray-700 border rounded shadow appearance-none focus:outline-none focus:shadow-outline"
          />
        </div>
        <div className="mb-2">
          <label
            htmlFor="lastName"
            className="block mb-1 font-bold text-gray-700"
          >
            Last Name:
          </label>
          <input
            type="text"
            id="lastName"
            name="lastName"
            value={state.lastName}
            onChange={handleChange}
            className="w-full px-2 py-1 leading-tight text-gray-700 border rounded shadow appearance-none focus:outline-none focus:shadow-outline"
          />
        </div>
        <div className="mb-2">
          <label htmlFor="email" className="block mb-1 font-bold text-gray-700">
            Email:
          </label>
          <input
            type="email"
            id="email"
            name="email"
            value={state.email}
            onChange={handleChange}
            className="w-full px-2 py-1 leading-tight text-gray-700 border rounded shadow appearance-none focus:outline-none focus:shadow-outline"
          />
        </div>
        <div className="mb-2">
          <label
            htmlFor="gender"
            className="block mb-1 font-bold text-gray-700"
          >
            Gender:
          </label>
          <select
            id="gender"
            name="gender"
            value={state.gender}
            onChange={handleChange}
            className="w-full px-2 py-1 leading-tight text-gray-700 border rounded shadow appearance-none focus:outline-none focus:shadow-outline"
          >
            <option value="">Select gender</option>
            <option value="male">Male</option>
            <option value="female">Female</option>
          </select>
        </div>
        <div className="mb-2">
          <label htmlFor="bio" className="block mb-1 font-bold text-gray-700">
            Bio:
          </label>
          <textarea
            id="bio"
            name="bio"
            value={state.bio}
            onChange={handleChange}
            className="w-full px-2 py-1 leading-tight text-gray-700 border rounded shadow appearance-none focus:outline-none focus:shadow-outline"
          />
        </div>
        <div className="flex justify-end">
          <button
            type="submit"
            onClick={handleSubmit}
            className="px-4 py-2 w-full text-white bg-blue-500 rounded-lg hover:bg-blue-700 focus:outline-none focus:shadow-outline-blue active:bg-blue-800"
          >
            Submit
          </button>
        </div>
      </form>
    </div>
  );
};

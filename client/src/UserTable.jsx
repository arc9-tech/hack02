import { useState, useEffect } from "react";
const API_URL = import.meta.env.VITE_API_URL;
const API_KEY = import.meta.env.VITE_API_KEY;

export const UserTable = ({ userData }) => {
  const [imageUrls, setImageUrls] = useState([]);

  useEffect(() => {
    const fetchImageUrls = async () => {
      const urls = await Promise.all(
          userData.map(async (user) => {
            const uploadUrlResponse = await fetch(
                `${API_URL}/user/${user.id}/dp-download-url`,
                {
                  headers: {
                    Authorization: `ApiKey ${API_KEY}`,
                  },
                }
            );
            const { url } = await uploadUrlResponse.json();
            return url;
          })
      );
      setImageUrls(urls);
    };
    fetchImageUrls();
  }, [userData]);

  const getResizedImageUrl = (url, maxWidth, maxHeight) => {
    const image = new Image();
    image.src = url;

    const width = image.width;
    const height = image.height;

    if (width <= maxWidth && height <= maxHeight) {
      return url;
    }

    const aspectRatio = width / height;
    if (width > maxWidth) {
      return `${url}?w=${maxWidth}&h=${Math.round(maxWidth / aspectRatio)}`;
    } else if (height > maxHeight) {
      return `${url}?h=${maxHeight}&w=${Math.round(maxHeight * aspectRatio)}`;
    }
  };

  return (
      <div className="text-center flex flex-col items-center gap-2">
        <h3 className="font-bold text-xl">User data table</h3>
        <table>
          <thead>
          <tr>
            <th>Image</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Email</th>
            <th>Gender</th>
            <th>Bio</th>
          </tr>
          </thead>
          <tbody>
          {userData.map((user, index) => (
              <tr key={user.id}>
                <td>
                  {imageUrls[index] && (
                      <img
                          src={getResizedImageUrl(imageUrls[index], 50, 50)}
                          alt={`Profile picture of ${user.firstName} ${user.lastName}`}
                          width="50"
                          height="50"
                      />
                  )}
                </td>
                <td>{user.firstName}</td>
                <td>{user.lastName}</td>
                <td>{user.email}</td>
                <td>{user.gender}</td>
                <td>{user.bio}</td>
              </tr>
          ))}
          </tbody>
        </table>
      </div>
  );
};

import requests as requests




if __name__ == '__main__':

    dummy_header = {
        'Authorization' : 'Something'
    }
    # API endpoint URLs
    dp_upload_url = "http://localhost:8080/user/tariq/dp-upload-url"
    dp_download_url = "http://localhost:8080/user/tariq/dp-download-url"

    # S3 object key and file path
    file_path = "data/ph.jpg"
    download_file_path = "data/downloaded.jpg"

    # Get the S3 upload URL from the API endpoint
    response = requests.get(dp_upload_url, headers=dummy_header)
    response.raise_for_status()
    response_json = response.json()
    upload_url = response_json["url"]
    expiration = response_json["expiredAt"]

    # Upload the file to the S3 upload URL
    with open(file_path, "rb") as f:
        response = requests.put(upload_url, data=f)
        response.raise_for_status()

    # Get the S3 download URL from the API endpoint
    response = requests.get(dp_download_url, headers=dummy_header)
    response.raise_for_status()
    response_json = response.json()
    download_url = response_json["url"]

    # Download the file from the S3 download URL and save it to disk
    response = requests.get(download_url)
    response.raise_for_status()
    with open(download_file_path, "wb") as f:
        f.write(response.content)

    # Verify that the downloaded file matches the original file
    with open(file_path, "rb") as f:
        original_content = f.read()
    with open(download_file_path, "rb") as f:
        downloaded_content = f.read()
    if original_content == downloaded_content:
        print("File upload and download succeeded!")
    else:
        print("File upload and download failed.")
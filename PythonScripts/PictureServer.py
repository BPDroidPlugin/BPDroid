#!/usr/bin/env python
# coding: utf-8

# In[ ]:


# SERVER IMPLEMENTATION

# Install Flask to create a simple HTTP server
# Command: pip install flask

from flask import Flask, request, jsonify, send_file
import os
import random

app = Flask(__name__)

# Directory containing the pictures
PICTURES_DIR = "/Users/lylan/UiO/Thesis/MyPaperFiles/Conferences/SECDroid-SEAA2025/pictures"  # Ensure this folder contains 5 pictures named pic1.jpg, pic2.jpg, ..., pic5.jpg

# Route to handle GET requests
@app.route('/test_get', methods=['GET'])
def handle_get():
    return jsonify({"message": "GET request received successfully!"}), 200

# Route to handle POST requests
@app.route('/test_post', methods=['POST'])
def handle_post():
    # Get the data sent in the request body
    data = request.get_json()
    return jsonify({"message": "POST request received successfully!", "data": data}), 200

# Route to send a random picture -> E.g. /get_picture?number=3
@app.route('/get_picture', methods=['GET'])
def get_picture():
    try:
        # Get the number from the query parameters
        number = request.args.get('number', type=int)

        if number is None:
            return jsonify({"error": "Number parameter is required."}), 400

        # List all picture files in the directory
        pictures = [f for f in os.listdir(PICTURES_DIR) if f.endswith(('.jpg', '.png', '.jpeg'))]
        
        if not pictures:
            return jsonify({"error": "No pictures found in the directory."}), 404

        # Find the corresponding picture (assuming naming format like pic<number>.png)
        picture_name = f"pic{number}.png"  # Adjust naming pattern if necessary

        if picture_name not in pictures:
            return jsonify({"error": f"Picture {picture_name} not found."}), 404

        # Get the full path of the picture
        picture_path = os.path.join(PICTURES_DIR, picture_name)

        # Send the file to the client
        return send_file(picture_path, mimetype='image/png')

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    # Run the Flask app on localhost at port 6500
    app.run(host='0.0.0.0', port=6500)


# In[ ]:





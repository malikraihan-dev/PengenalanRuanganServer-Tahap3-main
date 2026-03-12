"""
Python Face Recognition Microservice
=====================================
Layanan pendukung untuk Server Room Monitoring.
Menggunakan dlib / face_recognition library untuk encoding wajah.

Endpoints:
  POST /encode     - Encode wajah dari gambar base64, return 128-dim embedding
  POST /compare    - Bandingkan dua embeddings, return distance & match
  POST /detect     - Deteksi wajah dalam gambar, return bounding boxes
  GET  /health     - Health check

Jalankan: python face_service.py
Requires: pip install flask face_recognition numpy pillow

Catatan: Layanan ini optional. Sistem utama menggunakan face-api.js di browser.
         Gunakan service ini jika ingin server-side face encoding.
"""

from flask import Flask, request, jsonify
import face_recognition
import numpy as np
import base64
import io
from PIL import Image
import json
import logging

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# ======================== HELPERS ========================

def decode_base64_image(base64_string):
    """Decode base64 image string to numpy array."""
    # Remove data URL prefix if present
    if ',' in base64_string:
        base64_string = base64_string.split(',')[1]
    
    image_bytes = base64.b64decode(base64_string)
    image = Image.open(io.BytesIO(image_bytes))
    
    # Convert to RGB if necessary
    if image.mode != 'RGB':
        image = image.convert('RGB')
    
    return np.array(image)


def embedding_to_list(embedding):
    """Convert numpy embedding to Python list."""
    return embedding.tolist() if isinstance(embedding, np.ndarray) else list(embedding)


def euclidean_distance(emb1, emb2):
    """Calculate Euclidean distance between two embeddings."""
    a = np.array(emb1)
    b = np.array(emb2)
    return float(np.linalg.norm(a - b))


# ======================== ENDPOINTS ========================

@app.route('/health', methods=['GET'])
def health():
    """Health check endpoint."""
    return jsonify({
        'status': 'ok',
        'service': 'Face Recognition Microservice',
        'version': '1.0.0'
    })


@app.route('/encode', methods=['POST'])
def encode_face():
    """
    Encode wajah dari gambar base64.
    
    Request JSON:
        { "image": "<base64 image string>" }
    
    Response JSON:
        {
            "success": true,
            "faces_found": 1,
            "embedding": [0.123, -0.456, ...],   // 128-dim float array
            "all_embeddings": [[...], [...]]      // jika multiple faces
        }
    """
    try:
        data = request.get_json()
        if not data or 'image' not in data:
            return jsonify({'success': False, 'message': 'Field "image" (base64) diperlukan'}), 400
        
        # Decode image
        image_array = decode_base64_image(data['image'])
        
        # Detect face locations
        face_locations = face_recognition.face_locations(image_array, model='hog')
        
        if len(face_locations) == 0:
            return jsonify({
                'success': False,
                'faces_found': 0,
                'message': 'Tidak ada wajah terdeteksi dalam gambar'
            })
        
        # Get face encodings (128-dim vectors)
        face_encodings = face_recognition.face_encodings(image_array, face_locations)
        
        all_embeddings = [embedding_to_list(enc) for enc in face_encodings]
        
        return jsonify({
            'success': True,
            'faces_found': len(face_encodings),
            'embedding': all_embeddings[0],     # Primary face
            'all_embeddings': all_embeddings
        })
    
    except Exception as e:
        logger.error(f'Encode error: {str(e)}')
        return jsonify({'success': False, 'message': f'Error: {str(e)}'}), 500


@app.route('/compare', methods=['POST'])
def compare_faces():
    """
    Bandingkan dua face embeddings.
    
    Request JSON:
        {
            "embedding1": [0.123, -0.456, ...],
            "embedding2": [0.789, -0.012, ...]
        }
    
    Response JSON:
        {
            "success": true,
            "distance": 0.35,
            "confidence": 0.65,
            "match": true,
            "threshold": 0.6
        }
    """
    try:
        data = request.get_json()
        if not data or 'embedding1' not in data or 'embedding2' not in data:
            return jsonify({'success': False, 'message': 'embedding1 dan embedding2 diperlukan'}), 400
        
        emb1 = data['embedding1']
        emb2 = data['embedding2']
        threshold = data.get('threshold', 0.6)
        
        distance = euclidean_distance(emb1, emb2)
        confidence = max(0, 1 - distance)
        is_match = distance <= threshold
        
        return jsonify({
            'success': True,
            'distance': round(distance, 4),
            'confidence': round(confidence, 4),
            'match': is_match,
            'threshold': threshold
        })
    
    except Exception as e:
        logger.error(f'Compare error: {str(e)}')
        return jsonify({'success': False, 'message': f'Error: {str(e)}'}), 500


@app.route('/detect', methods=['POST'])
def detect_faces():
    """
    Deteksi wajah dalam gambar dan return bounding boxes.
    
    Request JSON:
        { "image": "<base64 image string>" }
    
    Response JSON:
        {
            "success": true,
            "faces_found": 2,
            "faces": [
                {"top": 100, "right": 300, "bottom": 350, "left": 150},
                ...
            ]
        }
    """
    try:
        data = request.get_json()
        if not data or 'image' not in data:
            return jsonify({'success': False, 'message': 'Field "image" diperlukan'}), 400
        
        image_array = decode_base64_image(data['image'])
        face_locations = face_recognition.face_locations(image_array, model='hog')
        
        faces = []
        for (top, right, bottom, left) in face_locations:
            faces.append({
                'top': top,
                'right': right,
                'bottom': bottom,
                'left': left,
                'width': right - left,
                'height': bottom - top
            })
        
        return jsonify({
            'success': True,
            'faces_found': len(faces),
            'faces': faces
        })
    
    except Exception as e:
        logger.error(f'Detect error: {str(e)}')
        return jsonify({'success': False, 'message': f'Error: {str(e)}'}), 500


@app.route('/match', methods=['POST'])
def match_face():
    """
    Match satu embedding terhadap daftar known embeddings.
    
    Request JSON:
        {
            "target_embedding": [0.123, ...],
            "known_faces": [
                {"nim": "12345", "nama": "Ahmad", "embedding": [0.456, ...]},
                {"nim": "12346", "nama": "Budi",  "embedding": [0.789, ...]}
            ],
            "threshold": 0.6
        }
    
    Response JSON:
        {
            "success": true,
            "matched": true,
            "best_match": {
                "nim": "12345",
                "nama": "Ahmad",
                "distance": 0.32,
                "confidence": 0.68
            }
        }
    """
    try:
        data = request.get_json()
        target = data.get('target_embedding')
        known_faces = data.get('known_faces', [])
        threshold = data.get('threshold', 0.6)
        
        if not target or not known_faces:
            return jsonify({'success': False, 'message': 'target_embedding dan known_faces diperlukan'}), 400
        
        best_match = None
        best_distance = float('inf')
        
        for face in known_faces:
            dist = euclidean_distance(target, face['embedding'])
            if dist < best_distance:
                best_distance = dist
                best_match = face
        
        if best_match and best_distance <= threshold:
            return jsonify({
                'success': True,
                'matched': True,
                'best_match': {
                    'nim': best_match.get('nim'),
                    'nama': best_match.get('nama'),
                    'distance': round(best_distance, 4),
                    'confidence': round(max(0, 1 - best_distance), 4)
                }
            })
        else:
            return jsonify({
                'success': True,
                'matched': False,
                'best_distance': round(best_distance, 4) if best_match else None,
                'message': 'Tidak ada wajah yang cocok'
            })
    
    except Exception as e:
        logger.error(f'Match error: {str(e)}')
        return jsonify({'success': False, 'message': f'Error: {str(e)}'}), 500


# ======================== MAIN ========================

if __name__ == '__main__':
    logger.info('='*60)
    logger.info('  Face Recognition Microservice')
    logger.info('  Server Room Monitoring System')
    logger.info('  Port: 5050')
    logger.info('='*60)
    app.run(host='0.0.0.0', port=5050, debug=True)

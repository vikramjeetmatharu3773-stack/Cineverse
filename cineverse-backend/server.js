const express = require('express');
const cors = require('cors');
const sqlite3 = require('sqlite3').verbose();
const axios = require('axios');
require('dotenv').config();

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(cors());
app.use(express.json());

// Database setup
const db = new sqlite3.Database('./movies.db');

// Initialize database tables
db.serialize(() => {
    db.run(`CREATE TABLE IF NOT EXISTS movies (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        title TEXT NOT NULL,
        description TEXT,
        poster_url TEXT,
        video_url TEXT NOT NULL,
        year INTEGER,
        tmdb_id INTEGER,
        created_at DATETIME DEFAULT CURRENT_TIMESTAMP
    )`);

    // Insert sample movies if database is empty
    db.get("SELECT COUNT(*) as count FROM movies", (err, row) => {
        if (err) {
            console.error('Error checking database:', err);
            return;
        }
        
        if (row.count === 0) {
            const sampleMovies = [
                {
                    title: "The Matrix",
                    description: "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.",
                    poster_url: "https://image.tmdb.org/t/p/w500/uKxWc4LgCCRBtcq4jY3hL4y4tLm.jpg",
                    video_url: "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_1mb.mp4",
                    year: 1999,
                    tmdb_id: 603
                },
                {
                    title: "Inception",
                    description: "A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
                    poster_url: "https://image.tmdb.org/t/p/w500/9gk7aEfG6Aa4eHkDL8z0J4l5aKm.jpg",
                    video_url: "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_2mb.mp4",
                    year: 2010,
                    tmdb_id: 27205
                }
            ];

            const insert = db.prepare("INSERT INTO movies (title, description, poster_url, video_url, year, tmdb_id) VALUES (?, ?, ?, ?, ?, ?)");
            sampleMovies.forEach(movie => {
                insert.run(movie.title, movie.description, movie.poster_url, movie.video_url, movie.year, movie.tmdb_id);
            });
            insert.finalize();
            console.log('Sample movies inserted into database');
        }
    });
});

// API Routes
app.get('/api/movies', (req, res) => {
    const query = "SELECT * FROM movies ORDER BY title";
    db.all(query, [], (err, rows) => {
        if (err) {
            res.status(500).json({ error: err.message });
            return;
        }
        res.json(rows);
    });
});

app.get('/api/movies/:id', (req, res) => {
    const id = req.params.id;
    const query = "SELECT * FROM movies WHERE id = ?";
    db.get(query, [id], (err, row) => {
        if (err) {
            res.status(500).json({ error: err.message });
            return;
        }
        if (!row) {
            res.status(404).json({ error: 'Movie not found' });
            return;
        }
        res.json(row);
    });
});

app.post('/api/movies', (req, res) => {
    const { title, description, poster_url, video_url, year, tmdb_id } = req.body;
    
    if (!title || !video_url) {
        res.status(400).json({ error: 'Title and video URL are required' });
        return;
    }

    const query = "INSERT INTO movies (title, description, poster_url, video_url, year, tmdb_id) VALUES (?, ?, ?, ?, ?, ?)";
    db.run(query, [title, description, poster_url, video_url, year, tmdb_id], function(err) {
        if (err) {
            res.status(500).json({ error: err.message });
            return;
        }
        res.json({ 
            id: this.lastID,
            title, 
            description, 
            poster_url, 
            video_url, 
            year, 
            tmdb_id 
        });
    });
});

// TMDB API integration (placeholder for now)
app.get('/api/tmdb/search', async (req, res) => {
    const { query } = req.query;
    const TMDB_API_KEY = process.env.TMDB_API_KEY || 'placeholder';
    
    try {
        const response = await axios.get(`https://api.themoviedb.org/3/search/movie`, {
            params: {
                api_key: TMDB_API_KEY,
                query: query
            }
        });
        res.json(response.data);
    } catch (error) {
        res.status(500).json({ error: 'Failed to search TMDB' });
    }
});

app.get('/api/tmdb/movie/:id', async (req, res) => {
    const { id } = req.params;
    const TMDB_API_KEY = process.env.TMDB_API_KEY || 'placeholder';
    
    try {
        const response = await axios.get(`https://api.themoviedb.org/3/movie/${id}`, {
            params: {
                api_key: TMDB_API_KEY
            }
        });
        res.json(response.data);
    } catch (error) {
        res.status(500).json({ error: 'Failed to fetch movie from TMDB' });
    }
});

// Health check
app.get('/health', (req, res) => {
    res.json({ status: 'OK', timestamp: new Date().toISOString() });
});

app.listen(PORT, () => {
    console.log(`Cineverse Backend API running on http://localhost:${PORT}`);
    console.log(`Available endpoints:`);
    console.log(`  GET /api/movies - List all movies`);
    console.log(`  GET /api/movies/:id - Get movie by ID`);
    console.log(`  POST /api/movies - Add new movie`);
    console.log(`  GET /api/tmdb/search - Search TMDB`);
    console.log(`  GET /api/tmdb/movie/:id - Get TMDB movie details`);
    console.log(`  GET /health - Health check`);
});
-- Test data for SimplePolls plugin
-- This file creates sample polls for testing the /poll command GUI

-- Insert test polls
INSERT INTO polls (id, question, created_by, is_open, created_at, end_at) VALUES
('poll-1', 'What is your favorite Minecraft biome?', 'admin', true, '2024-01-01 10:00:00', NULL),
('poll-2', 'Which mob should be added next?', 'admin', true, '2024-01-01 11:00:00', '2024-12-31 23:59:59'),
('poll-3', 'Best building block?', 'admin', true, '2024-01-01 12:00:00', NULL);

-- Insert poll options for poll-1 (Favorite biome)
INSERT INTO poll_options (id, poll_id, option_text, sort_order, icon_material) VALUES
('opt-1-1', 'poll-1', 'Forest', 1, 'OAK_LOG'),
('opt-1-2', 'poll-1', 'Desert', 2, 'SAND'),
('opt-1-3', 'poll-1', 'Ocean', 3, 'WATER_BUCKET'),
('opt-1-4', 'poll-1', 'Mountain', 4, 'STONE');

-- Insert poll options for poll-2 (Next mob)
INSERT INTO poll_options (id, poll_id, option_text, sort_order, icon_material) VALUES
('opt-2-1', 'poll-2', 'Penguin', 1, 'SNOW_BLOCK'),
('opt-2-2', 'poll-2', 'Crab', 2, 'RED_DYE'),
('opt-2-3', 'poll-2', 'Armadillo', 3, 'LEATHER');

-- Insert poll options for poll-3 (Building block)
INSERT INTO poll_options (id, poll_id, option_text, sort_order, icon_material) VALUES
('opt-3-1', 'poll-3', 'Stone Bricks', 1, 'STONE_BRICKS'),
('opt-3-2', 'poll-3', 'Oak Planks', 2, 'OAK_PLANKS'),
('opt-3-3', 'poll-3', 'Cobblestone', 3, 'COBBLESTONE'),
('opt-3-4', 'poll-3', 'Quartz', 4, 'QUARTZ_BLOCK');
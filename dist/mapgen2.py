import libtcodpy as libtcod
import math
import textwrap
import ConfigParser
import os
from datetime import datetime

class Object:
	#generic object class
	def __init__(self, x, y, char, name, color):
		self.x = x
		self.y = y
		self.char = char
		self.color = color
		self.name = name

	def move(self, dx, dy):
		self.x += dx
		self.y += dy

	def draw(self):
		libtcod.console_set_default_foreground(con, self.color)
		libtcod.console_put_char(con, self.x, self.y, self.char, libtcod.BKGND_NONE)

	def move_towards(self, target_x, target_y):
		#vector from this object to the target, and distance
		dx = target_x - self.x
		dy = target_y - self.y
		distance = math.sqrt(dx ** 2 + dy ** 2)

		#normalize it to length 1 (preserving direction), then round it and
		#convert to integer so the movement is restricted to the map grid
		dx = int(round(dx / distance))
		dy = int(round(dy / distance))
		self.move(dx, dy)

	def distance_to(self, other):
		#return distance to another object
		dx = other.x - self.x
		dy = other.y - self.y
		return math.sqrt(dx ** 2 + dy ** 2)

	def clear(self):
		#make the object vanish
		libtcod.console_put_char(con, self.x, self.y, ' ', libtcod.BKGND_NONE)

class Tile:
	def __init__(self, prob):
		global exp_color
		self.prob = prob
		if prob < 0.0001 or prob > 1:
			self.r, self.g, self.b = 0, 0, 0
		elif exp_color:
			if prob <= 0.001:
				self.r, self.g, self.b = 0, int(255*1000*prob), 255
			elif prob <= 0.01:
				self.r, self.g, self.b = 0, 255, int(255*(10 - 1000*prob)/9)
			elif prob <= 0.1:
				self.r, self.g, self.b = int(255*(100*prob - 1)/9), 255, 0
			else:
				self.r, self.g, self.b = 255, int(255*(1 - prob*10)/9), 0
		else:
			if prob <= 0.25:
				self.r, self.g, self.b = 0, int(255*4*prob), 255
			elif prob <= 0.5:
				self.r, self.g, self.b = 0, 255, int(255*(2- 4*prob))
			elif prob <= 0.75:
				self.r, self.g, self.b = int(255*4*(prob - 0.5)), 255, 0
			else:
				self.r, self.g, self.b = 255, int(255*(4- 4*prob)), 0

def strbool(string):
	if string == "True": return True
	if string == "False": return False
	return False #default value, feel free to change

def process_keys():
	global fov_recompute, current_map, current_path, show_path, show_viterbi, exp_color, caseNo, iterNo, viterbi_case, startloc, endloc

	key = libtcod.console_wait_for_keypress(True)
	if key.vk == libtcod.KEY_ESCAPE:
		return "exit"

	#movement keys
	if game_state == "playing":
		if libtcod.console_is_key_pressed(libtcod.KEY_UP):
			player_move_or_attack(0, -1)
		elif libtcod.console_is_key_pressed(libtcod.KEY_DOWN):
			player_move_or_attack(0, 1)
		elif libtcod.console_is_key_pressed(libtcod.KEY_LEFT):
			player_move_or_attack(-1, 0)
		elif libtcod.console_is_key_pressed(libtcod.KEY_RIGHT):
			player_move_or_attack(1, 0)
		elif key.c == ord('p'): #toggle path
			show_path = not show_path
		elif key.c == ord('l'): #toggle viterbi
			show_viterbi = not show_viterbi
		elif chr(key.c).isdigit():
			viterbi_case = int(chr(key.c))
		elif key.c == ord('-'):
			viterbi_case = -1
		elif key.c == ord('c'):
			exp_color = not exp_color
			render_map(current_map)
			read_path(current_path)
			read_viterbi()
		elif key.c == ord('q'):
			caseNo -= 1;
			objects.remove(startloc)
			objects.remove(endloc)
			current_path = os.path.join(os.path.dirname(__file__), '..', 'AI3', 'AI3Paths', 'Grid' + str((caseNo/10)%10) + '_Case' + str(caseNo % 10) + '.txt')
			current_map = os.path.join(os.path.dirname(__file__), '..', 'AI3', 'AI3Paths', 'Grid' + str((caseNo/10)%10) + '_Case' + str(caseNo % 10) + 'MatrixIterations', str(iterNo) + '.txt')
			render_map(current_map)
			read_path(current_path)
			read_viterbi()
		elif key.c == ord('e'):
			caseNo += 1;
			objects.remove(startloc)
			objects.remove(endloc)
			current_path = os.path.join(os.path.dirname(__file__), '..', 'AI3', 'AI3Paths', 'Grid' + str((caseNo/10)%10) + '_Case' + str(caseNo % 10) + '.txt')
			current_map = os.path.join(os.path.dirname(__file__), '..', 'AI3', 'AI3Paths', 'Grid' + str((caseNo/10)%10) + '_Case' + str(caseNo % 10) + 'MatrixIterations', str(iterNo) + '.txt')
			render_map(current_map)
			read_path(current_path)
			read_viterbi()
		elif key.c == ord('a'):
			iterNo = 10;
			objects.remove(startloc)
			objects.remove(endloc)
			current_path = os.path.join(os.path.dirname(__file__), '..', 'AI3', 'AI3Paths', 'Grid' + str((caseNo/10)%10) + '_Case' + str(caseNo % 10) + '.txt')
			current_map = os.path.join(os.path.dirname(__file__), '..', 'AI3', 'AI3Paths', 'Grid' + str((caseNo/10)%10) + '_Case' + str(caseNo % 10) + 'MatrixIterations', str(iterNo) + '.txt')
			render_map(current_map)
			read_path(current_path)
			read_viterbi()
		elif key.c == ord('s'):
			iterNo = 50;
			objects.remove(startloc)
			objects.remove(endloc)
			current_path = os.path.join(os.path.dirname(__file__), '..', 'AI3', 'AI3Paths', 'Grid' + str((caseNo/10)%10) + '_Case' + str(caseNo % 10) + '.txt')
			current_map = os.path.join(os.path.dirname(__file__), '..', 'AI3', 'AI3Paths', 'Grid' + str((caseNo/10)%10) + '_Case' + str(caseNo % 10) + 'MatrixIterations', str(iterNo) + '.txt')
			render_map(current_map)
			read_path(current_path)
			read_viterbi()
		elif key.c == ord('d'):
			iterNo = 99;
			objects.remove(startloc)
			objects.remove(endloc)
			current_path = os.path.join(os.path.dirname(__file__), '..', 'AI3', 'AI3Paths', 'Grid' + str((caseNo/10)%10) + '_Case' + str(caseNo % 10) + '.txt')
			current_map = os.path.join(os.path.dirname(__file__), '..', 'AI3', 'AI3Paths', 'Grid' + str((caseNo/10)%10) + '_Case' + str(caseNo % 10) + 'MatrixIterations', str(iterNo) + '.txt')
			render_map(current_map)
			read_path(current_path)
		else: return "didnt-take-turn"

	#action keys
	else:
		key_char = chr(key.c)
		print key_char


def player_move_or_attack(dx, dy):
	global fov_recompute
	x, y = player.x + dx, player.y + dy

	player.move(dx, dy)
	fov_recompute = True

def read_path(filename): #actual (ground truth) path
	global path, startloc, endloc
	path = []
	f = open(filename, 'r')

	#read the tuples one by one (hurrah, hurrah)
	for line in f:
		if not line: continue
		if len(path) < 101:
			data = line.split(',')
			x, y = int(data[0]), int(data[1])
			path.append((x, y))

	#process start/end position of agent and add him to the map
	startloc = Object(path[0][0], path[0][1], 'S', 'start', libtcod.white)
	endloc = Object(path[-1][0], path[-1][1], 'E', 'end', libtcod.white)
	objects.append(startloc)
	objects.append(endloc)

def render_map(filename):
	global map, current_map
	#TODO: make this better for arbitrarily large numbers of tilesets
	f = open(filename, 'r')

	# #process all the coordinates and add them to the map
	# startpos, endpos = f.readline(), f.readline()
	# sx, sy = [int(n) for n in startpos.split(',')]
	# ex, ey = [int(n) for n in endpos.split(',')]
	# startloc = Object(sx, sy, 'S', 'start', libtcod.white)
	# endloc = Object(ex, ey, 'E', 'end', libtcod.white)
	# objects.append(startloc)
	# objects.append(endloc)

	map = [[None for y in range(MAP_HEIGHT+1)] for x in range(MAP_WIDTH)]
	for row in range(MAP_HEIGHT):
		line = f.readline().strip()
		data = line.split(",")
		for col in range(MAP_WIDTH):
			map[col][row] = Tile(-1)
		for col in range(len(data)):
			if data[col]: map[col][row] = Tile(float(data[col]))
	#generate a row to show heat map variety
	for col in range(MAP_WIDTH):
		map[col][100] = Tile(float(col)/100)

	#generate start/end backgrounds
	# map[sx][sy] = Tile('S', tileset, config)
	# map[ex][ey] = Tile('E', tileset, config)
	f.close()


def render_all():
	global fov_map, fov_recompute, game_msgs

	for y in range(MAP_HEIGHT):
		for x in range(MAP_WIDTH):
			libtcod.console_put_char_ex(con, x, y, ' ', libtcod.Color(int(map[x][y].r), int(map[x][y].g), int(map[x][y].b)), libtcod.Color(int(map[x][y].r), int(map[x][y].g), int(map[x][y].b)))
			if show_path and (x, y) in path: #start and end locations have special markers
				libtcod.console_put_char_ex(con, x, y, 'X', libtcod.Color(255, 0, 0), libtcod.Color(int(map[x][y].r), int(map[x][y].g), int(map[x][y].b)))
			for v in range(len(viterbi)):
				if show_viterbi and (x, y) in viterbi[v]:
					if viterbi_case >= 0 and viterbi_case != v: continue
					libtcod.console_put_char_ex(con, x, y, str(v), libtcod.Color(255, 0, 0), libtcod.Color(int(map[x][y].r), int(map[x][y].g), int(map[x][y].b)))
			#libtcod.console_set_char_background(con, x, y, libtcod.Color(int(map[x][y].r), int(map[x][y].g), int(map[x][y].b)), libtcod.BKGND_SET)

	game_msgs = [msg for msg in game_msgs if msg[4] == True]
	message('GRID ' + str(caseNo/10 % 10).zfill(1), 70, 0, libtcod.yellow)
	message('CASE ' + str(caseNo % 10).zfill(1), 80, 0, libtcod.yellow)
	message('CELL (' + str(player.x) + ',' + str(player.y) + ')', 70, 1, libtcod.white, False)
	message('Probability: ' + str(map[player.x][player.y].prob), 70, 2, libtcod.cyan, False)

	p_obj = objects.pop(objects.index(player))
	for object in objects:
		object.draw()
	p_obj.draw() #draw player last
	objects.append(player)

	#prepare to render messages
	libtcod.console_set_default_background(panel, libtcod.black)
	libtcod.console_clear(panel)

	for (line, color, x, y, always_on) in game_msgs:
		libtcod.console_set_default_foreground(panel, color)
		libtcod.console_print_ex(panel, x, y, libtcod.BKGND_NONE, libtcod.LEFT, line) #coordinate info

	libtcod.console_blit(con, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, 0)
	libtcod.console_blit(panel, 0, 0, SCREEN_WIDTH, PANEL_HEIGHT, 0, 0, PANEL_Y)

def is_blocked(x, y):
	if map[x][y].blocked:
		return True
	for object in objects:
		if object.blocks and object.x == x and object.y == y:
			return True
	return False

def message(new_msg, x, y, color = libtcod.white, always_on = True):
	new_msg_lines = textwrap.wrap(new_msg, MSG_WIDTH)

	for line in new_msg_lines:
		game_msgs.append((line, color, x, y, always_on))

def read_viterbi():
	global caseNo, iterNo
	for i in range(10):
		if iterNo == 99: iterNo = 100
		current_viterbi = os.path.join(os.path.dirname(__file__), '..', 'AI3', 'AI3Paths', 'Grid' + str((caseNo/10)%10) + '_Case' + str(caseNo % 10) + 'ViterbiPathIterations', 'Iter' + str(iterNo) + '_Likely' + str(i) + '.txt')
		if iterNo == 100: iterNo = 99
		#read the tuples one by one (hurrah, hurrah)
		f = open(current_viterbi, 'r')
		viterbi[i] = []
		for line in f:
			if not line: continue
			data = line.split(',')
			x, y = int(data[0]), int(data[1])
			viterbi[i].append((x, y))

SCREEN_WIDTH = 100
SCREEN_HEIGHT = 106
MAP_WIDTH = 100
MAP_HEIGHT = 101
caseNo = 0
iterNo = 10

#constants for information display
PANEL_HEIGHT = 4
PANEL_Y = SCREEN_HEIGHT - PANEL_HEIGHT
MSG_WIDTH = SCREEN_WIDTH
MSG_HEIGHT = 1

libtcod.console_set_custom_font('courier8x8_aa_tc.png', libtcod.FONT_LAYOUT_TCOD)
libtcod.console_init_root(SCREEN_WIDTH, SCREEN_HEIGHT, 'python/map', False)
con = libtcod.console_new(SCREEN_WIDTH, SCREEN_HEIGHT)

player = Object(50, 50, '@', 'player', libtcod.white)
objects = [player]
path = []
show_path = show_viterbi = False
exp_color = False
viterbi_case = -1
game_msgs = []

current_path = os.path.join(os.path.dirname(__file__), '..', 'AI3', 'AI3Paths', 'Grid' + str((caseNo/10)%10) + '_Case' + str(caseNo % 10) + '.txt')
current_map = os.path.join(os.path.dirname(__file__), '..', 'AI3', 'AI3Paths', 'Grid' + str((caseNo/10)%10) + '_Case' + str(caseNo % 10) + 'MatrixIterations', str(iterNo) + '.txt')
viterbi = [[] for i in range(10)]
render_map(current_map)
read_path(current_path)
read_viterbi()

fov_map = libtcod.map_new(MAP_WIDTH, MAP_HEIGHT)
fov_recompute = True
#for y in range(MAP_HEIGHT):
	#for x in range(MAP_WIDTH):
		#libtcod.map_set_properties(fov_map, x, y, not map[x][y].block_sight, not map[x][y].blocked)

game_state = "playing"
player_action = None

panel = libtcod.console_new(SCREEN_WIDTH, PANEL_HEIGHT)
message('CONTROLS:', 4, 0, libtcod.white)
message('- USE THE ARROW KEYS TO MOVE THE CURSOR (@) AROUND', 8, 1, libtcod.red)
message('- USE (L) TO SHOW/HIDE THE VITERBI PATHS', 8, 2, libtcod.yellow)
message('- USE (P) TO SHOW/HIDE THE ACTUAL PATH', 8, 3, libtcod.cyan)
message('CURRENTLY VIEWING:', 50, 0, libtcod.white)

#main loop
while not libtcod.console_is_window_closed():
	render_all()

	libtcod.console_flush()

	#get next instance of movement
	for object in objects:
		object.clear()

	player_action = process_keys()
	if player_action == "exit": break

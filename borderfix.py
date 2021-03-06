"""
Usage:
    borderfix (file|folder) [options] <name>...

Options:
    -s BORDER_SIZE_PIXELS, --size BORDER_SIZE_PIXELS

Notes:
    Folders are not crawled recursively
"""

from docopt import docopt
from PIL import Image
import os
import ntpath

BORDERFIX_DIR = os.getcwd() + "/borderfix/"

def process_image(filepath, size):
    try:
        image = Image.open(filepath)
        newsize = (image.size[0] + size*2, image.size[1] + size*2)
        newimage = Image.new("RGBA", newsize, (0, 0, 0, 0))

        for x in range(0, image.size[0]):
            for y in range(0, image.size[1]):
                imgxy = (x, y)
                newxy = (x+size, y+size)
                pixel = image.getpixel(imgxy)
                newimage.putpixel(newxy, pixel)
        filename = ntpath.basename(filepath)
        newimage.save(BORDERFIX_DIR + filename, "png")
    except IOError:
        print("Unable to open file as image: " + ntpath.basename(filepath))

def process_folder(filepath, size):
    for filename in os.listdir(filepath):
        filename = filepath + filename
        if os.path.isfile(filename):
            print("Add border to " + filename)
            process_image(filename, size)


def getsize(args):
    size = args.get("--size")
    if size == None:
        size = 1
    return int(size)

def process(args):
    if args.get("file"):
        for name in args.get("<name>"):
            process_image(name, getsize(args))
    elif args.get("folder"):
        for name in args.get("<name>"):
                process_folder(name, getsize(args))

if __name__ == "__main__":
    args = docopt(__doc__)
    if not os.path.exists(BORDERFIX_DIR):
        os.makedirs(BORDERFIX_DIR)
    process(args)

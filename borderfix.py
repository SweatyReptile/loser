"""
Usage: 
    borderfix (file|folder) [options] <name>...
    
Options:
    -s BORDER_SIZE_PIXELS, --size BORDER_SIZE_PIXELS
"""

from docopt import docopt
from PIL import Image

def process_image(filepath, size):
    try:
        image = Image.open(filepath)
    except:
        print("Unable to find file: ", filepath)
    
    size = size-1
    newsize = (image.size[0] + size*2, image.size[1] + size*2)
    newimage = Image.new(image.mode, image.size)

    for x in xrange(0, newimage.size[0]):
        for y in xrange(0, newimage.size[1]):
            if (x > size and y > size):
                imgxy = (x-size, y-size)
                newxy = (x, y)
                pixel = image.getpixel(imgxy)
                newimage.putpixel(newxy, pixel)
    newimage.show()
    
def process(args):
    if args.get("file"):
        for name in args.get("<name>"):
            process_image(name, int(args.get("--size")))
    elif args.get("folder"):
        print("folder")
        
if __name__ == "__main__":
    args = docopt(__doc__)
    print(args)
    process(args)
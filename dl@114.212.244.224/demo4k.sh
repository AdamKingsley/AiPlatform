rm images -rf;
rm results -rf;
mkdir images && mkdir results;
cd images;
axel -n 1 http://freebigpictures.com/wp-content/uploads/shady-forest.jpg --output=content1.png;
axel -n 1 https://vignette.wikia.nocookie.net/strangerthings8338/images/e/e0/Wiki-background.jpeg/revision/latest?cb=20170522192233 --output=style1.png;

# do not resize
#convert -resize 25% content1.png content1.png;
#convert -resize 50% style1.png style1.png;

cd ..;

output_image=./results/example1.png
content_image=./images/content1.png

#./NvBlit -w 3840 -l 2160 -i 700 -s ./images/style1.png -c $content_image -o $output_image -x "~/anaconda/bin/python demo4k.py --style_image_path @style_image --content_image_path @content_image --output_image_path @output_image"
./NvBlit -w 3840 -l 2160 -i 700 -s ./images/style1.png -c $content_image -o $output_image -x "python demo4k.py --style_image_path @style_image --content_image_path @content_image --output_image_path @output_image"



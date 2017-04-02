#!/usr/bin/env ruby

# Grab a few avatars from adorable.io

require 'net/http'
require 'json'

components = JSON.parse(Net::HTTP.get(URI('https://api.adorable.io/avatars/list')));

eyes = components['face']['eyes']
nose = components['face']['nose']
mouth = components['face']['mouth']

colors = ['1abc9c', '16a085', '2ecc71', '27ae60', '3498db', '2980b9',
          '9b59b6', '8e44ad', 'f1c40f', 'f39c12',
          'e67e22', 'd35400', 'e74c3c', 'c0392b']

names = ['alpha', 'bravo', 'charlie', 'delta', 'echo', 'foxtrot', 'golf',
         'hotel', 'india', 'juliet', 'kilo', 'lima', 'mike', 'november', 'oscar',
         'papa', 'quebec', 'romeo', 'sierra', 'tango', 'uniform', 'victor',
         'whiskey', 'x-ray', 'yankee', 'zulu']

threads = []

names.each_with_index do |name, i|
	face = [eyes[i % eyes.length], nose[i % nose.length], mouth[i % mouth.length], colors[i % colors.length]]

	threads << Thread.new do
		File.open("#{name}.png", "wb") do |f|
			contents = Net::HTTP.get(URI("https://api.adorable.io/avatars/face/#{face.join('/')}"))
			f.write(contents)
		end
		
		`convert #{name}.png -resize 128x128 #{name}.png`
		`pngout #{name}.png` # optimise png
	end
end

threads.each { |t| t.join }

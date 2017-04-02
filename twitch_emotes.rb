#!/usr/bin/env ruby

require 'net/http'
require 'json'

dir = 'src/main/resources/images/emotes'
emotes = JSON.parse!(Net::HTTP.get(URI('https://twitchemotes.com/api_cache/v2/global.json')))

small_uri = emotes['template']['small']

threads = []
emotes_map = {}

emotes['emotes'].each do |k, v|
	emotes_map[k] = "#{k}.png"
	threads << Thread.new do
		File.open("#{dir}/#{k}.png", 'wb') do |f|
			image_id = v['image_id'].to_s
			contents = Net::HTTP.get(URI(small_uri.gsub('{image_id}', image_id)))
			f.write contents
		end
		
		`pngout #{k}.png`
	end
end

threads.each { |t| t.join }

File.open("#{dir}/emotes.properties", 'wb') do |f|
	f.puts "# Poker Pro 16 emotes"
	f.puts "# Totally not lifted from twitch"
	emotes_map.each do |k, v|
		f.puts "#{k}=#{v}"
	end
end

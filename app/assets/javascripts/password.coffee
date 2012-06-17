#   Source http://benjaminsterling.com/wp-content/files/passwordstrength.htm

$.fn.passwordStrength = (options) ->
  getPasswordStrength = (H) ->
    D = (H.length)
    D = 5  if D > 5
    F = H.replace(/[0-9]/g, "")
    G = (H.length - F.length)
    G = 3  if G > 3
    A = H.replace(/\W/g, "")
    C = (H.length - A.length)
    C = 3  if C > 3
    B = H.replace(/[A-Z]/g, "")
    I = (H.length - B.length)
    I = 3  if I > 3
    E = ((D * 10) - 20) + (G * 10) + (C * 15) + (I * 10)
    E = 0  if E < 0
    E = 100  if E > 100
    E

  randomPassword = ->
    chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$_+"
    size = 15
    i = 1
    ret = ""
    while i <= size
      $max = chars.length - 1
      $num = Math.floor(Math.random() * $max)
      $temp = chars.substr($num, 1)
      ret += $temp
      i++
    ret

  return @each(->
    that = this
    that.opts = {}
    that.opts = $.extend({}, $.fn.passwordStrength.defaults, options)
    that.div = $(that.opts.targetDiv)
    that.defaultClass = that.div.attr("class")
    that.percents = (if (that.opts.classes.length) then 100 / that.opts.classes.length else 100)
    v = $(this).keyup(->
      @el = $(this)  if typeof el is "undefined"
      s = getPasswordStrength(@value)
      p = @percents
      t = Math.floor(s / p)
      t = @opts.classes.length - 1  if 100 <= s
      @div.removeAttr("class").addClass(@defaultClass).addClass @opts.classes[t]
    ).after("<a href=\"#\">Generate Password</a>").next().click(->
      $(this).prev().val(randomPassword()).trigger "keyup"
      false
    )
  )

$.fn.passwordStrength.defaults =
  classes: Array("is10", "is20", "is30", "is40", "is50", "is60", "is70", "is80", "is90", "is100")
  targetDiv: "#passwordStrengthDiv"
  cache: {}

$(document).ready ->
  $("input[name=\"password\"]").passwordStrength()
  $("input[name=\"passwordGenerated\"]").passwordStrength
    targetDiv: "#passwordStrengthDiv2"
    classes: Array("is10", "is20", "is30", "is40")